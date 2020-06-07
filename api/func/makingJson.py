from collections import OrderedDict
import json

def makeNoticeJSON(noticeTable):
    table = []

    # table을 JSON 형태로 재정렬
    for t in noticeTable:
        jsonTable = OrderedDict()
        jsonTable["title"] = t[0]
        jsonTable["link"] = t[1]
        table.append(jsonTable)

    return table

def makeCafeteriaMenuJSON(menuTable):

    jsonTable = OrderedDict()
    jsonTable["breakfast"] = menuTable[0]
    jsonTable["lunch"] = menuTable[1]
    jsonTable["dinner"] = menuTable[2]

    return jsonTable