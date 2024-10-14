import sys
import gen as G
import random


def swap_ran(pair):
    if randint(0,1) == 1:
        pair[0], pair[1] = pair[1]*(-1), pair[0]*(-1)

if __name__ == "__main__":
    if len(sys.argv) != 6:
        # print("Usage: generator.py N RANGE_A_LOW RANGE_A_HIGH RANGE_B_LOW RANGE_B_HIGH file SEED")
        print("Usage: generator.py N TYPE file SEED ID")
        print("N - number of chunks")
        print("TYPE - (S)mall (F)ull range (O)verflow")
        print(list(sys.argv))
        sys.exit(1)
    
    n = int(sys.argv[1])
    type = sys.argv[2]
    file_name = sys.argv[3]
    seed = int(sys.argv[4])
    id = int(sys.argv[5])

    random.seed(seed)

    assert n > 0, f"Czemu n takie dziwne? n={n}"

    INT_MAX = (1<<64)-1
    DATA = [[],[]] # rangeA, rangeB
    match type:
        case "S": # use with N = 1...
            DATA = [[1, 1000], [1, 20]]
            if random.randint(0,1) == 1:
                DATA[0] = [G.int_to_twos(-DATA[0][1], 64), G.int_to_twos(-DATA[0][0], 64)]
            if random.randint(0,1) == 1:
                DATA[1] = [G.int_to_twos(-DATA[1][1], 64), G.int_to_twos(-DATA[1][0], 64)]
        case "F":
            DATA = [[0, INT_MAX], [0, INT_MAX]] # x 2^32 y 2^8
        case "O":
            DATA = [[1<<63, 1<<63], [INT_MAX, INT_MAX]]
        case _:
            print("What do you mean by:", sys.argv[1],"???")
            sys.exit(0)

    RANGE_A, RANGE_B = DATA[0], DATA[1]

    f = open(f'./{file_name}', 'w')
    sys.stdout , old_std = f, sys.stdout

    gg = G.GenerateTest(n, RANGE_A, RANGE_B)
    gg.set_seed(seed)
    gg.run_test(id)

    f.close()
    sys.stdout = old_std
    # print(f"Test {id} generated successfully. File {file_name}, seed {seed}")

'''
on students:
python3 generator.py 1 S "królik.txt" 1 69
'''

