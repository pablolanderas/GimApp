from genDomainFunctions import quitSymbols, typeInAtribute, getAtributeOriginalType, getAtributeValuesFromLine, getFunctionsFromLines, argumentsToUML

class Atribute:

    name: str
    type: str
    isPublic: bool

    def __init__(self, name, type, isPublic) -> None:
        self.name = name
        self.type = type
        self.isPublic = isPublic

class Arguments:

    name: str
    type: str

    def __init__(self, name, type) -> None:
        self.name = name
        self.type = type

class Function:

    name: str
    type: str
    args: list[Arguments]
    
    def __init__(self, name, type, args) -> None:
        self.name = name
        self.type = type
        self.args = args

class Element:

    lines: list[str]
    name: str = "Elemento sin definir"
    
    def __init__(self) -> None:
        self.lines = []

    def isSelf(e):
        raise NotImplemented()
    
    def getUmlTextClass(self, voidClasses: list[str]) -> str:
        raise NotImplemented()    

    def getUmlTextAssociations(self, otherClasses: list[str]) -> str:
        raise NotImplemented()      

class KotlinEnum(Element):

    values: list[str]
    atributes: list[Atribute]
    functions: list[Function]

    def getAtributeFromLine(l: str) -> Atribute:
        name, type, isPublic = getAtributeValuesFromLine(l)
        return Atribute(name, type, isPublic)
    
    def getValueFromLine(l: str) -> str:
        return l[:-1].split("(")[0].split()[0]

    def __init__(self, element: Element) -> None:
        self.lines = element.lines
        self.name = quitSymbols(self.lines[0].split()[2])
        # Get values
        valLines = []
        linesCopy = self.lines.copy()
        end = False
        started = False
        while linesCopy and not end:
            l = linesCopy.pop(0)
            if started: 
                valLines.append(l)
                if ";" in l: end = True
            elif "{" in l: started = True 
        self.values = list(map(KotlinEnum.getValueFromLine, valLines))
        # Get the atribues
        atrLines = filter(lambda x:"val" in x or "var" in x, self.lines)
        self.atributes = list(map(KotlinEnum.getAtributeFromLine, atrLines))
        # Get the functions
        self.functions = getFunctionsFromLines(self.lines)

    def isSelf(e: Element):
        return "class" in e.lines[0] and "enum" in e.lines[0]
    
    def getUmlTextClass(self, voidClasses: list[str]) -> str:
        text = f"enum {self.name}" + "{\n"
        for v in self.values:
            text += f"   {v}\n"
        if self.atributes:
            text += "\n"
        for atr in self.atributes:
            text += f"  + {atr.name} : {atr.type}\n"
        if self.functions:
            text += "\n"
        for fun in self.functions:
            text += f"  + {fun.name}({', '.join(argumentsToUML(fun.args))}) : {atr.type}\n"
        return text + "}\n"
    
    def getUmlTextAssociations(self, otherClasses: list[str]) -> str:
        return ""

class KotlinClass(Element):
    
    atributes: list[Atribute]
    functions: list[Function]

    def getAtributeFromLine(l: str) -> Atribute:
        name, type, isPublic = getAtributeValuesFromLine(l)
        return Atribute(name, type, isPublic)

    def __init__(self, element: Element) -> None:
        self.lines = element.lines
        self.name = quitSymbols(self.lines[0].split()[1])
        # Get the atribues
        atrLines = filter(lambda x:"val" in x or "var" in x, self.lines)
        self.atributes = list(map(KotlinClass.getAtributeFromLine, atrLines))
        # Get the functions
        self.functions = getFunctionsFromLines(self.lines)

    def isSelf(e: Element):
        return "class" in e.lines[0] and not "enum" in e.lines[0]
    
    def getUmlTextClass(self, voidClasses: list[str]) -> str:
        text = f"class {self.name} " + "{\n"
        for atr in filter(lambda x:not typeInAtribute(voidClasses, x), self.atributes):
            text += "    "
            text += "+ " if atr.isPublic else "- "
            text += f"{atr.name} : {atr.type}\n"
        if self.functions:
            text += "\n"
        for fun in self.functions:
            text += f"  + {fun.name}({', '.join(argumentsToUML(fun.args))}) : {atr.type}\n"
        return text + "}\n"
    
    def getUmlTextAssociations(self, otherClasses: list[str]) -> str:
        text = ""
        for atr in filter(lambda x:typeInAtribute(otherClasses, x), self.atributes):
            text += f"{self.name} --> {getAtributeOriginalType(atr)} : {atr.name}\n"
        return text


from sys import modules
from inspect import getmembers, isclass

def getEspecificElemnt(e: Element) -> Element:
    current_module = modules[__name__]
    for name, obj in getmembers(current_module, isclass):
        if issubclass(obj, Element) and obj is not Element:
            if obj.isSelf(e): return obj(e)
        
    raise ValueError("Elemento no encontrado")