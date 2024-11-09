from os import scandir, path
from genDomainFunctions import isOpenSymbol, isCloseSymbol, getOpenSymbol, typeInAtribute
from genDomainClasses import Element, getEspecificElemnt
import pyperclip

DIR_DOMAIN = "app\\src\\main\\java\\com\\example\\gimapp\\domain"

# Check if the dir exist
if not path.exists(DIR_DOMAIN):
    print(f"No existe el path: {DIR_DOMAIN}")
    exit(-1)

# Read the files 
with scandir(DIR_DOMAIN) as entries:
    files = [open(entry.path, 'r').read() for entry in entries if entry.is_file()]
print("Cargados los archivos")

elements: list[Element] = []
for file in files:
    # Quit the spaces and imports
    file = map(lambda x:x.strip(), file.split("\n"))
    file = filter(lambda x: x != "" and not "import " in x and not "package " in x, file)

    # Separate the elements
    openSymbols = []
    actualElement = Element()
    for index, l in enumerate(file):
        # Add the line to the elemen
        actualElement.lines.append(l)
        for e in l:
            if isOpenSymbol(e):
                openSymbols.append(e)
            if isCloseSymbol(e):
                last = openSymbols.pop(-1)
                # Exit if error
                if last != getOpenSymbol(e):
                    print(f"Se ha cerrado un simbolo que no se esperaba: {index}: \"{e}\"")
                    exit(-1)
        # Case end element
        if not openSymbols:
            elements.append(actualElement)
            actualElement = Element()

# Generate the specific elements
elements = list(map(lambda x:getEspecificElemnt(x), elements))
elementsNames = list(map(lambda x:x.name, elements))
print("Generados los elementos")

# Generate the file
fileText = "@startuml\n\n"
associations = ""
for e in elements:
    fileText += e.getUmlTextClass(elementsNames) + "\n"
    associations += e.getUmlTextAssociations(elementsNames)
fileText += associations + "\n"
fileText += "@enduml"
print("Creado el archivo")

r = input("Pulsa enter para copiarlo en el portapapeles o escribe cualquier cosa para escribirlo por pantalla  ")
if r: print(fileText)
else: pyperclip.copy(fileText)

