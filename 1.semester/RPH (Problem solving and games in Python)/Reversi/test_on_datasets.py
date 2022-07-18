from reversi_io import ReversiIO

from reversi_try import MyPlayer
import os, sys
import time

######################################################################
# possible setting different parameters         
class_name = MyPlayer  # class name of player to test
dataset = 'dataset'  # location of folder with datasets 
move_time_limit_ms = 1000.0  # time limit for move
######################################################################

class ReturnErrorCodes:
    '''Enum class for test_move return status.'''
    MOVE_OK = 0
    WRONG_MOVE = 1
    OVERTIME = 2
    WRONG_MOVE_FORMAT = 3
    
def print_board(board):
    '''
    Function for printing game board to console.
    '''
    for x in range(len(board)):
        row_string = ''
        for y in range(len(board[x])):
            if board[x][y] == -1:
                row_string += ' -'
            else:
                row_string += ' ' + str(board[x][y])
        print(row_string)

def test_move(move_data):
    '''
    Tests the class_name player move on move_data for wrong moves and overtimes.
    :param move_data: Dictionary with player, board and moves to test on player
    '''
    player_id = move_data['player']
    board = move_data['board']
    possible_moves = move_data['moves']
    player = None
    try:
        player = class_name(player_id, (player_id + 1) % 2)
        print('MyPlayer object created')
    except:
        print('constructor of MyPlayer failed and MyPlayer object can not be created!')
        sys.exit(1)
    start_time = time.time()
    player_move = player.move(board)
    endTime = time.time()
    #q = multiprocessing.Queue()
    #p = multiprocessing.Process(target=get_player_move, args=(q, player, board,))
    #p.start()
    # Wait for 1 second
    if( (endTime - start_time)*1000.0 > move_time_limit_ms):
        print("move is running too long '>%0.2fms'"%move_time_limit_ms)
        return (ReturnErrorCodes.OVERTIME, move_data)

    if (player_move not in possible_moves) or (player_move is None):
        print('-------------------------------------')
        print('Wrong move:')
        print(player_move)
        print('For player ' + str(player_id) + ' and board:')
        print_board(board)
        print('possible moves are:')
        print(possible_moves)
        print('-------------------------------------')  
        return (ReturnErrorCodes.WRONG_MOVE, move_data)
    
    if type(player_move) is not tuple :
        print('-------------------------------------')
        print('Wrong move format:')
        print(type(player_move))
        print('-------------------------------------')  
        return (ReturnErrorCodes.WRONG_MOVE_FORMAT, move_data)

    elif len(player_move) != 2 :
        print('-------------------------------------')  
        print('Wrong move size:')
        print(len(player_move))
        print('-------------------------------------')  
        return (ReturnErrorCodes.WRONG_MOVE_FORMAT, move_data)

    return (ReturnErrorCodes.MOVE_OK, move_data)
   

if __name__ == "__main__":
    num_ok = 0
    num_overtimes = 0
    num_wrong_moves = 0
    num_wrong_moves_format = 0
    for root, dirs, filenames in os.walk(dataset):
        for idx, file_name in enumerate(filenames):
            move_data = ReversiIO.load_state_with_moves(dataset + os.path.sep + file_name)
            print('test %i/%i'%(idx,len(filenames)-1))
            move_status = test_move(move_data)
            if move_status[0] == ReturnErrorCodes.OVERTIME:
                num_overtimes += 1
            elif move_status[0] == ReturnErrorCodes.WRONG_MOVE:
                num_wrong_moves += 1
            elif move_status[0] == ReturnErrorCodes.WRONG_MOVE_FORMAT:
                num_wrong_moves_format += 1
            else:
                num_ok += 1
                
    if num_overtimes == 0 and num_wrong_moves == 0  and num_wrong_moves_format == 0:
        print('All moves on dataset are OK')      
    else:
        print('Some moves on data set were wrong or overtime:')   
        print('Ok moves:' + str(num_ok))      
        print('Wrong moves:' + str(num_wrong_moves))   
        print('Wrong move formats:' + str(num_wrong_moves_format))   
        print('Moves take too long:' + str(num_overtimes))      

