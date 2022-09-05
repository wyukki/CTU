import random

def generate_blood():    
    data = []
    id = 0
    for i in range(35000):
        blood_Rh = random.randint(0, 1)
        bool = False
        if blood_Rh == 1:
            bool = True
        bloodGroup = random.randint(1, 4)
        #if bool:
         #   random.seed(blood_Rh)
        data.append((id, str(bloodGroup), str(bool)))
        id += 1
    return data

blood = generate_blood()
print(blood)
