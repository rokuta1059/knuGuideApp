from func import *

table = []
dormitoryTarget = ["새롬, 이룸, 재정"]

name = "새롬"

for c in dormitoryTarget:
    if c in name:
        table = func.cafeteriaMenu(name)

print(table)

data = {'dormitoryName': name,
    'result': [
        {
            'breakfast': table[0],
            'lunch': table[1],
            'dinner': table[2]
        }
    ]
}

print(data)