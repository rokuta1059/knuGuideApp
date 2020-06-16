import os
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


class departmentOffice(Resource):
    """
    학과사무실 주소랑 전화번호 받아오는 클래스
    - 주소값: /department/office/{학과아이디}
    """
    def get(self, department_id):
        officeTable = func.get_department_office(department_id)
        parser = reqparse.RequestParser()
        parser.add_argument('content', type=list)

        return { "department": department_id,
            "content": officeTable}

class departmentNotice(Resource):
    """
    학과 공지사항 받아오는 클래스
    - 주소값: /department/notice/{학과아이디}
    """
    def get(self, department_id):
        table = func.get_department_func(department_id)

        parser = reqparse.RequestParser()
        parser.add_argument('department', type=str)
        parser.add_argument('notice', type=list)

        return {"department": department_id, "notice": table}

class cafeteriaMenu(Resource):
    """
    식단 받아오는 클래스
    - 주소값: /cafeteria/{식당이름}
    """
    
    def get(self, name):
        table = []
        dormitoryTarget = ["새롬", "이룸", "재정"]
        for c in dormitoryTarget:
            if c in name:
                table = func.make_cafeteria_menu_json(func.make_diet_table(name))

        return {'dormitoryName': name,
            'result': table
        }

class universitySchedule(Resource):
    """
    학교의 학사일정을 받아오는 클래스
    - 주소값: /university/schedule
    """

    def get(self):
        schedule = func.make_academic_schedule_json(func.aca_schedule())

        return {'year': 2020,
            'content': schedule
        }

api.add_resource(myTestApi, '/')
api.add_resource(testMulti, '/multi/<int:num>')
api.add_resource(departmentOffice, '/department/office/<department_id>')
api.add_resource(departmentNotice, '/department/notice/<department_id>')
api.add_resource(cafeteriaMenu, '/cafeteria/<name>')
api.add_resource(universitySchedule, '/university/schedule')

if __name__ == '__main__':
    print(os.getcwd())
    f = open('value.txt', 'r')
    host = f.read().split()
    print('{0}:{1}'.format(host[0], host[1]))
    app.run(host=host[0],port=host[1],debug=True)