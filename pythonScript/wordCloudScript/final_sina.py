from wordcloud import WordCloud
import matplotlib.pyplot as plt
from PIL import Image
import numpy as np
import jieba as jb
import collections 

text = open('/Users/syy/Desktop/Hackthon/Project/analysis/pythonScript/WeiboSpider-search/filtered_result.json',encoding='utf-8').read()

seg_list = jb.cut(text,cut_all=False)
# text_cut='/'.join(seg_list)

tf={}

remove_words = [u'的', u'，',u'和', u'是', u'随着', u'对于',u'对',u'等',u'能',u'都',u'。',u' ',u'、',u'中',u'在',u'了',u'通常',u'如果',u'我们',u'需要', u'发表',u'可以',u'可能',u'就是',u'使用',u'博文',u'11',u'这个',u'2017',u'问题']

for seg in seg_list: 
    if seg not in remove_words:
        if seg in tf:
            tf[seg]+=1
        else:
            tf[seg]=1
ci=list(tf.keys())

for seg in ci:
    if tf[seg]<5 or len(seg)<2 or '一'in seg:
        tf.pop(seg)

word_counts = collections.Counter(tf) # 对分词做词频统计
word_counts_top30 = word_counts.most_common(30) # 获取前30最高频的词
print(word_counts_top30)


background = np.array(Image.open('xin.gif'))

wordcloud = WordCloud(background_color="white",width=1000,height=860,margin=2,
                      font_path="/Library/Fonts/Songti.ttc",mask=background).fit_words(tf)

plt.imshow(wordcloud)
plt.axis("off")
plt.show()

wordcloud.to_file('Weibo_Oracle_Java_Final.jpg')
