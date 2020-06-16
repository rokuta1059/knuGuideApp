from collections import OrderedDict
import json

def make_department_office_json(data):
    """
        학과 목록을 JSON 형태로 반환하는 함수
    """
    table = []
    for t in data:
        jsonTable = OrderedDict()
        jsonTable["college"] = t['college']
        jsonTable["department"] = t['department']
        jsonTable["callnumber"] = t['callnumber']
        jsonTable["location"] = t['location']
        jsonTable["map"] = t['map']
        jsonTable["site"] = t['site']
        table.append(jsonTable)

    return table

def make_notice_json(noticeTable):
    """
        공지사항을 JSON 형태로 반환하는 함수
        - noticeTable: 받아 온 해당 학과의 공지사항 배열
    """
    table = []

    # table을 JSON 형태로 재정렬
    for t in noticeTable:
        jsonTable = OrderedDict()
        jsonTable["department"] = t[0]
        jsonTable["number"] = t[1]
        jsonTable["title"] = t[2]
        jsonTable["date"] = t[3]
        jsonTable["link"] = t[4]
        table.append(jsonTable)

    return table

def make_cafeteria_menu_json(menuTable):
    """
        식단을 JSON 형태로 반환하는 함수
        - menuTable: 메뉴가 저장된 배열
    """

    jsonTable = OrderedDict()
    jsonTable["breakfast"] = menuTable[0]
    jsonTable["lunch"] = menuTable[1]
    jsonTable["dinner"] = menuTable[2]

    return jsonTable