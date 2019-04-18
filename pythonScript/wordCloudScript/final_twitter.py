#!/Library/Frameworks/Python.framework/Versions/3.7/bin/python3
from wordcloud import WordCloud,STOPWORDS
import matplotlib.pyplot as plt
from PIL import Image
import numpy as np


text=open('Twitter_Oracle_Java_Text.json', 'rb').read().decode('utf-8')

wc=WordCloud(background_color="white",width=1000,height=860,margin=2)

process_word=WordCloud.process_text(wc,text)
sort=sorted(process_word.items(),key=lambda e:e[1],reverse=True)
print(sort[:50])

stopwords=set(STOPWORDS)
#stopwords.add('Oracle')
stopwords.add('pic')
stopwords.add('twitter')
stopwords.add('https')
stopwords.add('buff')
stopwords.add('ly')

background = np.array(Image.open('xin.gif'))

wc=WordCloud(background_color="white",width=1000,height=860,mask=background,stopwords=stopwords,margin=2).generate(text)

plt.imshow(wc)
#plt.axis("off")
#plt.show()

wc.to_file('Twitter_Oracle_Java_Final.jpg')



