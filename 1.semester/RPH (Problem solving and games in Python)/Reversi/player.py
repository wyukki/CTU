DIRECTION = ((-1, -1), (-1, 0), (-1, 0), (0, -1), (0, 1), (1, -1), (1, 0), (1, 1))
ROWS = 8
COLUMNS = 8
AVAILABLE_SPACE = -1
POSITION_WEIGHTS = [
    [120, -20, 20, 5, 5, 20, -20, 120],
    [-20, -40, -5, -5, -5, -5, -40, -20],
    [20, -5, 15, 3, 3, 15, -5, 20],
    [5, -5, 3, 3, 3, 3, -5, 5],
    [5, -5, 3, 3, 3, 3, -5, 5],
    [20, -5, 15, 3, 3, 15, -5, 20],
    [-20, -40, -5, -5, -5, -5, -40, -20],
    [120, -20, 20, 5, 5, 20, -20, 120]
]


class MyPlayer:
    """Local maximization strategy"""

    def __init__(self, my_color, opponent_color):
        self.best_move_coord = []
        self.best_move = []
        self.possible_move = []
        self.name = "semenvol"
        self.my_color = my_color
        self.opponent_color = opponent_color

    def find_my_stone(self, board):
        my_stone = self.my_color
        for row in range(0, ROWS):
            for column in range(0, COLUMNS):
                if board[row][column] == my_stone:
                    self.move_dir(row, column, board)

    def move_dir(self, row, column, board):
        """Find opp_stone and if there is available space return position"""
        opp_stone = self.opponent_color
        for direction in DIRECTION:
            count = 1
            r = row + direction[0]
            c = column + direction[1]
            if is_on_board(r, c):
                if board[r][c] == opp_stone:
                    while is_on_board(r, c) and board[r][c] == opp_stone:
                        count += 1
                        r = row + direction[0] * count
                        c = column + direction[1] * count

                    if is_on_board(r, c) and board[r][c] == AVAILABLE_SPACE:
                        self.possible_move.append([r, c])

    def local_maximization(self):
        for i in self.possible_move:
            r = i[0]
            c = i[1]
            self.best_move.append(POSITION_WEIGHTS[r][c])  # take the value from PW
            self.best_move_coord.append([r, c])
        self.find_max_best_move()
        self.find_max_best_move_coord()

    def find_max_best_move(self):
        for j in range(0, len(self.best_move)):
            if len(self.best_move) > 1:
                if self.best_move[0] <= self.best_move[1]:
                    del self.best_move[0]  # if there is bigger/equal number, delete smaller/equal
                elif self.best_move[1] <= self.best_move[0]:
                    del self.best_move[1]

    def find_max_best_move_coord(self):
        tmp_best_move_coord = self.best_move_coord.copy()
        for k in range(0, len(tmp_best_move_coord)):
            r = tmp_best_move_coord[k][0]
            c = tmp_best_move_coord[k][1]
            if POSITION_WEIGHTS[r][c] != self.best_move[0]:
                del self.best_move_coord[0]
            else:
                for i in tmp_best_move_coord:
                    if i == i[0]:
                        del self.best_move_coord[1]


    def move(self, board):
        """If there is not possible moves return None"""
        self.find_my_stone(board)
        self.local_maximization()
        print(self.best_move)
        print(self.best_move_coord)
        print(self.possible_move)
        if None in self.best_move_coord:
            return None
        else:
            return self.best_move_coord[0][0], self.best_move_coord[0][1]


def is_on_board(r, c):
    return 0 <= r <= 7 and 0 <= c <= 7
