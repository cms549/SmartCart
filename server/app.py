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
	occupancy = occ
	if request.method == 'GET':
		return render_template('test.html', data=occupancy)
	if request.method == "POST":
		global occ
		#update the occupancy from the database 
		occ= occ+1
		occupancy = occ
		return render_template('test.html', data=occupancy)

@app.route('/leave', methods=['GET', 'POST'])
def leave():
	global occ
	#update the occupancy from the database 
	occ= occ-1
	occupancy = occ
	return render_template('test.html', data=occupancy)
		

		
@app.route('/types', methods=['GET', 'POST'])
def types():
	if request.method == "POST":
		type = request.form['type']
		#search for specific topic
		itemss = db.items.find({'type': type})
		t=[]
		for r in range(0, itemss.count()):
			k= itemss.next()
			nameOfItem = k.get("name")
			price = k.get("price")
			bc = k.get("barcode")
			dat = nameOfItem +'$' +price+'$'+bc
			t.append( dat)
		return render_template('test.html', data=t)
	#grab the list of types from the database
	types = db.types.find()
	t=[]
	for r in range(0, types.count()):
		k= types.next()
		nameOfItem = k.get("name")
		t.append( nameOfItem)
	return render_template('test.html', data=t)
	
	
@app.route('/items', methods=['GET', 'POST'])
def items():
	if request.method == 'GET':
		#grab the items from the database
		its = db.items.find()
		t=[]
		for r in range(0, its.count()):
			k= its.next()
			nameOfItem = k.get("name")
			price = k.get("price")
			bc = k.get("barcode")
			dat = nameOfItem +'$' +price+'$'+bc
			t.append( dat)
		return render_template('test.html', data=t)
	if request.method == "POST":
		bc = int(request.form['barcode'])
		#search for specific number
		ans = db.items.find_one_or_404({'barcode': str(bc)})
		return render_template('test.html', data=ans)
		
		
@app.route('/itemsearch', methods=['POST'])
def itemsearch():
	if request.method == "POST":
		#search by keyword
		keyword = request.form['keyword']
		print "Keyword=="+keyword
		#search for  keyword
		#db.users.findOne({"username" : /.*son.*/i});
		ans = db.items.find_one_or_404({'name': keyword})
		return render_template('test.html', data=ans)	
	
        
@app.route('/location', methods=['GET', 'POST'])
def location():
	if request.method == 'GET':
		#grab the rfid locaitons from the database
		its = db.location.find()
		t=[]
		for r in range(0, its.count()):
			k= its.next()
			rf = k.get("rfid")
			x = k.get("x")
			y = k.get("y")
			t.append(rf+"$"+x+"$"+y )
		return render_template('test.html', data=t)
	if request.method == "POST":
		idnum = request.form['rfid']
		print idnum
		#search for specific number
		ans = db.location.find_one_or_404({'rfid': idnum})
		return render_template('test.html', data=ans)

if __name__ == '__main__':
	app.run()