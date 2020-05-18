from . import departmentnotice as dn

"""
    학과 이름을 받아온 후
    해당하는 학과에 맞는 함수를 호출한다.
"""
def getDepartmentFunc(department):
    if department == "컴퓨터공학과":
        return dn.departmentComputerNotice()
    elif department == "경영학전공":
        return dn.departmentBizNotice()