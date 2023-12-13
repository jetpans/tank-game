import math
operators = {}


operators[-1] = lambda a,b: a


operators[0] =  lambda a, b: a+b
operators[1] =  lambda a, b: a-b
operators[2] =  lambda a, b: a*b
operators[3] =  lambda a, b: a/b if b != 0 else 0
operators[4] =  lambda a, b: a%b if b != 0 else 0
operators[5] =  lambda a, b: a // b if b != 0 else 0
operators[6] =  lambda a, b: max(a,b)
operators[7] =  lambda a, b: min(a,b)
operators[8] =  lambda a, b: min(a**b, 1e6)
operators[9] =  lambda a, b: math.sin(a)
operators[10] = lambda a, b: math.cos(a)
operators[11] = lambda a, b: a**0.5 if a >= 0 else (-a)**0.5
operators[12] = lambda a, b: a**2