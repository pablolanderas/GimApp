import re

symbols = {
    ")": "(",
    "}": "{"
}

def isOpenSymbol(e) -> bool:
    return e in symbols.values()

def isCloseSymbol(e) -> bool:
    return e in symbols.keys()

def getOpenSymbol(e) -> str:
    return symbols[e]

def quitSymbols(text: str) -> str:
    for k, v in symbols.items():
        text = text.replace(k, "")
        text = text.replace(v, "")
    return text

def getAtributeOriginalType(atribute) -> str:
    aType = atribute.type
    aType = aType.replace("?", "")
    if "<" in aType:
        aType = aType.split("<")[1].split(">")[0]
    return aType

def typeInAtribute(types, atribute) -> bool:
    aType = getAtributeOriginalType(atribute)
    return aType in types

def getAtributeValuesFromLine(l: str) -> (str, str, bool):
    if l[-1] == ",": l = l[:-1]
    return (
        l.split(":")[0].split()[-1], 
        l.split(":")[1].split()[0], 
        True
    )

def calculateTheFunction(text: str):
    from genDomainClasses import Function, Arguments
    name = text.split()[1].split("(")[0]
    argsText = ")".join("(".join(text.split("(")[1:]).split(")")[:-1])
    argsText = re.sub(r'\(.*?\)', '', argsText)
    args = []
    for arg in argsText.split(","):
        if arg.replace(" ", ""):
            argName = arg.split(":")[0].replace(" ", "")
            type = arg.split(":")[1].split()[0]
            args.append(Arguments(argName, type))
    noArgs = re.sub(r'\(.*?\)', '', text)
    type = "Void" if len(v:=noArgs.split(":")) == 1 else v[1].split()[0]
    return Function(name, type, args)

def getFunctionsFromLines(lines: list[str]):
    functionText = None
    lines = lines.copy()
    opensAvoid = 0
    functions = []
    while lines:
        l = lines.pop(0)
        if functionText is None:
            if l[:4] == "fun ":
                functionText = ""
                opensAvoid -= 1
        if functionText is not None:
            functionText += l
        opensAvoid += l.count("(")
        opensAvoid -= l.count(")")
        if opensAvoid < 0:
            functions.append(calculateTheFunction(functionText))
            functionText = None
            opensAvoid = 0
    return functions

def argumentsToUML(arguments) -> list[str]:
        return map(lambda x: f"{x.name}: {x.type}", arguments)



