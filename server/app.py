#!flask/bin/python
from flask import Flask, request, render_template
from flask_pymongo import MongoClient

#connection = MongoClient('mongodb://localhost:27017/')
mongo_server = "localhost"
mongo_port = "27017"
connect_string = "mongodb://"+ mongo_server+ ":"+ mongo_port

app = Flask(__name__)
connection = MongoClient(connect_string)    
db = connection.test



occ = 0
			
@app.route('/occupancy', methods=['GET', 'POST'])
def occupancy():
	#grab the occupancy from the database
	occupancy = ""+occ
	if request.method == 'GET':
		return render_template('test.html', data=occupancy)
	if request.method == "POST":
		global occ
		#update the occupancy from the database 
		occ= occ+1
		occupancy = ""+occ
		return render_template('test.html', data=occupancy)

@app.route('/leave', methods=['GET', 'POST'])
def leave():
	global occ
	#update the occupancy from the database 
	occ= occ-1
	occupancy = ""+occ
	return render_template('test.html', data=occupancy)
		

		
@app.route('/topics', methods=['GET', 'POST'])
def topics():
	if request.method == "POST":
		topic = request.form['topic']
		#search for specific topic
		ans = db.topics.find_one_or_404({'topic': topic})
		return render_template('test.html', data=ans)
	#grab the list of topics from the database
	topics = db.topics.find()
	t=[]
	for r in range(0, topics.count()):
		t.append( topics.next().get("name"))
	return render_template('test.html', data=t)
	
	
@app.route('/items', methods=['GET', 'POST'])
def items():
	if request.method == 'GET':
		#grab the items from the database
		its = db.items.find().toArray()
		t=[]
		for r in range(0, its.count()):
			t.append( its.next())
		return render_template('test.html', data=t)
	if request.method == "POST":
		idnum = int(request.form['idnum'])
		if idnum != -1:
			#search for specific number
			ans = db.items.find({'_id': idnum})
			return render_template('test.html', data=ans)
		#search by keyword
		keyword = request.form['keyword']
		#search for  keyword
		#db.users.findOne({"username" : /.*son.*/i});
		ans = db.items.find({'name': keyword})
		return render_template('test.html', data=ans)
        

if __name__ == '__main__':
	app.run()