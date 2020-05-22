import sys
from flask import Flask, request
from flask_restful import Resource, Api, reqparse
from collections import OrderedDict
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

"""
    학과사무실 주소랑 전화번호 받아오는 클래스
    주소값: /department/office
    !! 코드 작성만 했음 상세 코드 미구현으로 인해 테스트 미실시 !!
"""
class departmentOffice(Resource):
    def get(self):
        table = []
        officeTable = func.getDepartmentOffice()
        for t in officeTable:
            jsonTable = OrderedDict()
            jsonTable["department"] = t[0]
            jsonTable["address"] = t[1]
            jsonTable["number"] = t[2]
            table.append(jsonTable)

        parser = reqparse.RequestParser()
        parser.add_argument('content', type=list)

        return {"content": table}

    def post(self):
        department_json = request.get_json()
        data = func.getDepartmentOffice(department_json["department"])

        parser = reqparse.RequestParser()
        parser.add_argument('department', type=str)
        parser.add_argument('address', type=str)
        parser.add_argument('number', type=str)

        return {"department": data[0],
            "address": data[1],
            "number": data[2] }


"""
    학과 공지사항 받아오는 클래스
    주소값: /department/notice/{학과이름}
"""
class departmentNotice(Resource):
    def get(self, department):
        noticeTable = func.getDepartmentFunc(department)
        table = []

        # table을 JSON 형태로 재정렬
        for t in noticeTable:
            jsonTable = OrderedDict()
            jsonTable["title"] = t[0]
            jsonTable["link"] = t[1]
            table.append(jsonTable)

        parser = reqparse.RequestParser()
        parser.add_argument('department', type=str)
        parser.add_argument('notice', type=list)

        return {"department": department, "notice": table}

"""
    식단 받아오는 클래스
    주소값: /cafeteria/{식당이름}
"""
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

class usiversitySchedule(Resource):

    def get(self):
        table = []
        schedule = func.acaSchedule()

api.add_resource(myTestApi, '/')
api.add_resource(testMulti, '/multi/<int:num>')
api.add_resource(departmentOffice, '/department/office')
api.add_resource(departmentNotice, '/department/notice/<string:department>')
api.add_resource(cafeteriaMenu, '/cafeteria/<string:name>')

if __name__ == '__main__':
    f = open('value.txt', 'r')
    host = f.readline()
    port = f.readline()
    app.run(host=host,port=port,debug=True)