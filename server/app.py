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
		

		
@app.route('/types', methods=['GET', 'POST'])
def types():
	if request.method == "POST":
		type = request.form['type']
		#search for specific topic
		ans = db.types.find_one_or_404({'type': type})
		return render_template('test.html', data=ans)
	#grab the list of types from the database
	types = db.types.find()
	t=[]
	for r in range(0, types.count()):
		t.append( type.next().get("name"))
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
        
@app.route('/location', methods=['GET', 'POST'])
def location():
	if request.method == "POST":
		idnum = int(request.form['rfid'])
		if idnum != -1:
			#search for specific number
			ans = db.location.find({'rfid': idnum})
			return render_template('test.html', data=ans)
	#grab the rfid locaitons from the database
		its = db.location.find().toArray()
		t=[]
		for r in range(0, its.count()):
			t.append( its.next())
		return render_template('test.html', data=t)
		
		

if __name__ == '__main__':
	app.run()