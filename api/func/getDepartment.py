from . import departmentnotice as dn
from . import crawling as cw
from . import makingJson as mj

"""
    학과 이름을 받아온 후
    해당하는 학과에 맞는 함수를 호출한다.
    cbaTable 등은 임시로 제작
"""

cbaTable = ["경영대", "경제학전공", "관광경영학과", "식품생명공학과", "식물자원응용과학전공",
    "응용생물학전공", "원예과학전공", "농업자원경제학과", "지역건설공학과", "스포츠과학과", 
    "체육교육과", "산림대", "산림자원학전공", "산림소재공학전공", "생태조경디자인학과", 
    "약대", "의생대", "분자생명과학과", "생명건강공학과", "생물의소재공학과",
    "의생명융합학부", "시스템면역과학전공", "의생명공학전공", "영어영문학과", "독어독문학전공", 
    "중어중문학전공", "사학전공"]

bizTable = ["경영학전공", "산림환경보호학전공", "제지공학전공", "수의과"]

def getDepartmentFunc(department):
    if department in cbaTable:
        return mj.makingNoticeJSON(cw.cba())
    elif department in bizTable:
        return mj.makingNoticeJSON(cw.biz())

# def getDepartmentFunc(department):
#     if department == "컴퓨터공학과":
#         return dn.departmentComputerNotice()
#     elif department == "경영학전공":
#         return dn.departmentBizNotice()