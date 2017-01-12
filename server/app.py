from flask import Flask, request
from dbfunctions import *

app = Flask(__name__)
			
			
@app.route('/occupancy', methods=['GET', 'POST'])
def occupancy():
	#grab the occupancy from the database
	occ = ""+grabOccupancy()
    if request.method == 'GET':
        return occ
    if request.method == "POST":
        #update the occupancy from the database 
		updateOccupancy(1)
		return occ
		
@app.route('/topics', methods=['GET', 'POST'])
def topics():
	if request.method == "POST":
		topic = request.form['topic']
        #search for specific topic
		ans = searchByTopic(topic)
		return ans
	#grab the list of topics from the database
	topics = grabTopics()
    return topics
	
	
@app.route('/items', methods=['GET', 'POST'])
def items():
    if request.method == 'GET':
		#grab the items from the database
		its = grabAllItems()
        return its
    if request.method == "POST":
		idnum = int(request.form['idnum'])
		if idnum != -1
			#search for specific number
			ans = searchByNum(idnum)
			return ans
		#search by keyword
		keyword = request.form['keyword']
		#search for  keyword
		ans = searchByKeyword(keyword)
		return ans
        

if __name__ == '__main__':
    app.run()