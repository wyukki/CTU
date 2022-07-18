class MyVector:

    def __init__(self, coordinates):
        self.coordinates = coordinates

    def get_vector(self):
        return self.coordinates

    def __mul__(self, other):
        scalar = 0
        for i, j in zip(self.coordinates, other.coordinates):
            scalar += i*j
        return scalar

    def is_perpendicular_to(self, other):
        if vec1.__mul__(other) == 0:
            return True
        else:
            return False


if __name__ == "__main__":
    vec1 = MyVector([1, 0, 0])
    vec2 = MyVector([0, 1, 0])
    print(vec1.is_perpendicular_to(vec2))
