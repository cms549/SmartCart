from flask import Flask, request
from dbfunctions import *
from flask.ext.pymongo import PyMongo

app = Flask(__name__)
mongo = PyMongo(app)

# connect to another MongoDB database on the same host
#app.config['TYPES'] = 'types'
#mongo2 = PyMongo(app, config_prefix='MONGO2')

occ = 0
			
@app.route('/occupancy', methods=['GET', 'POST'])
def occupancy():
	#grab the occupancy from the database
	occupancy = ""+occ
    if request.method == 'GET':
        return occupancy
    if request.method == "POST":
        global occ
		#update the occupancy from the database 
		occ++
		occupancy = ""+occ
		return occupancy

@app.route('/leave', methods=['GET', 'POST'])
def leave():
    global occ
	#update the occupancy from the database 
	occ--
	occupancy = ""+occ
	return occupancy
		

		
@app.route('/topics', methods=['GET', 'POST'])
def topics():
	if request.method == "POST":
		topic = request.form['topic']
        #search for specific topic
		ans = mongo.db.topics.find_one_or_404({'topic': topic})
		return ans
	#grab the list of topics from the database
	topics = mongo.db.topics.find({})
    return topics
	
	
@app.route('/items', methods=['GET', 'POST'])
def items():
    if request.method == 'GET':
		#grab the items from the database
		its = mongo.db.items.find({})
        return its
    if request.method == "POST":
		idnum = int(request.form['idnum'])
		if idnum != -1
			#search for specific number
			ans = mongo.db.items.find({'_id': idnum})
			return ans
		#search by keyword
		keyword = request.form['keyword']
		#search for  keyword
		#db.users.findOne({"username" : /.*son.*/i});
		ans = mongo.db.items.find({'name': keyword})
		return ans
        

if __name__ == '__main__':
    app.run()