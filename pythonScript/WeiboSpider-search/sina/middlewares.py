# encoding: utf-8
import random

import pymongo
from sina.settings import LOCAL_MONGO_PORT, LOCAL_MONGO_HOST, DB_NAME


class CookieMiddleware(object):
    """
    每次请求都随机从账号池中选择一个账号去访问
    """

    def __init__(self):
        # client = pymongo.MongoClient(LOCAL_MONGO_HOST, LOCAL_MONGO_PORT)
        # self.account_collection = client[DB_NAME]['account']
        tmp = 1

    def process_request(self, request, spider):
        # all_count = self.account_collection.find({'status': 'success'}).count()
        # if all_count == 0:
        #     raise Exception('当前账号池为空')
        # random_index = random.randint(0, all_count - 1)
        # random_account = self.account_collection.find({'status': 'success'})[random_index]
        # request.headers.setdefault('Cookie', random_account['cookie'])
        cookie = "_T_WM=203e7ff224076edfa5d307874da11954; SSOLoginState=1555594997; SCF=ArRQ0SWfiIKqC4wwb3msJIUYgHY6XikLxpLc0QCnd10jb8LJVU0aoV5nI3cFtgNNGBr5twoRavyHSPqvjCcLiTg.; SUHB=0M2VKQ3iXa_mNL; SUB=_2A25xvA6lDeRhGeFO71MW9i7MzD-IHXVTXpLtrDV6PUJbkdANLUPakW1NQXiR3Cruhb9pXzXNyMukJst5_zOlFMc3"
        request.headers.setdefault('Cookie', cookie)
        account = {"_id": "lianglan8209@163.com", "password": "FKVlao3563q", "cookie": cookie, "status": "success"}
        request.meta['account'] = account


class RedirectMiddleware(object):
    """
    检测账号是否正常
    302 / 403,说明账号cookie失效/账号被封，状态标记为error
    418,偶尔产生,需要再次请求
    """

    def __init__(self):
        client = pymongo.MongoClient(LOCAL_MONGO_HOST, LOCAL_MONGO_PORT)
        self.account_collection = client[DB_NAME]['account']

    def process_response(self, request, response, spider):
        http_code = response.status
        if http_code == 302 or http_code == 403:
            self.account_collection.find_one_and_update({'_id': request.meta['account']['_id']},
                                                        {'$set': {'status': 'error'}}, )
            return request
        elif http_code == 418:
            spider.logger.error('ip 被封了!!!请更换ip,或者停止程序...')
            return request
        else:
            return response
