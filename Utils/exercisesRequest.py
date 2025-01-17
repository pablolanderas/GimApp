import http.client
import json
import requests
import pprint

""" 
----------------- La peticion de ejercicios a la API -----------------

conn = http.client.HTTPSConnection("exercisedb.p.rapidapi.com")

headers = {
    'x-rapidapi-key': "43b4051cfbmsh07a9cdb13fea429p1b08c5jsnac9d693fcb2a",
    'x-rapidapi-host': "exercisedb.p.rapidapi.com"
}

conn.request("GET", "/exercises?limit=10000", headers=headers) #/bodyPart/back?limit=10&offset=0

res = conn.getresponse()
dataText = res.read().decode("utf-8")
data = json.loads(dataText)

with open("Utils/descargaDB.json", "w") as file:
    json.dump(data, file, indent=4)

print(len(data))
"""

def translate(text: str) -> str:
    url = "http://localhost:5000/translate"
    payload = {
        "q": text,
        "source": "en",
        "target": "es",
        "format": "text",
        "alternatives": 0,
        "api_key": ""
    }
    headers = {
        "Content-Type": "application/json"
    }

    response = requests.post(url, data=json.dumps(payload), headers=headers)
    return response.json()['translatedText']

def translateTT(text):
    if type(text) != str: 0/0
    return text + " la traduccion"

with open("Utils/descargaDB.json", "r") as file:
    data = json.loads(file.read())

"""
for index, exercise in enumerate(data):
    print(f" -- Traduciendo {index+1}/{len(data)} --")
    t = " ".join(translate(f"Exercise: {exercise['name']}").split()[1:])
    exercise["nombre"] = t

with open("Utils/descargaDBTraducido.json", "w") as file:
    json.dump(data, file, indent=4)
"""

for e in set(map(lambda x:x["bodyPart"], data)): print(translate(e))