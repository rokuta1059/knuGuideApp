import sys
from flask import Flask, request
from flask_restful import Resource, Api
import json
import func

app = Flask(__name__)
api = Api(app)
api.app.config['RESTFUL_JSON'] = {
    'ensure_ascii': False
}

class myTestApi(Resource):
    def get(self):
        return {'status': 'hello'}
    
    def post(self):
        test_json = request.get_json()
        return {'sent': test_json}, 205

class testMulti(Resource):
    def get(self, num):
        return {'result': num * 10}

class cafeteriaMenu(Resource):
    
    def get(self, name):
        table = []
        dormitoryTarget = ["새롬", "이룸", "재정"]
        for c in dormitoryTarget:
            if c in name:
                table = func.makeDietTable(name)

        return {'dormitoryName': name,
            'result': [
                {
                    'breakfast': table[0],
                    'lunch': table[1],
                    'dinner': table[2]
                }
            ]
        }

api.add_resource(myTestApi, '/')
api.add_resource(testMulti, '/multi/<int:num>')
api.add_resource(cafeteriaMenu, '/cafeteria/<string:name>')

if __name__ == '__main__':
    app.run(debug=True)