from random import randint, seed
import sys

'''
a / b ---> b * x + r = a
'''

# assuming num is unsigned value, convert it as if it was a u2 number (SIGNING)
# interpret uint as bits in U2
def twos_to_int(num, bits):
    if num & (1 << (bits-1)) != 0:
        num -= (1 << (bits))
    return num

# convert number in U2 to its bits
def int_to_twos(num, bits):
    if num < 0:
        num = (1<<(bits)) + num
    return num


# bi-Number represented in binary (twos complement) and its numeric value
class Bimber():
    def __init__(self, bits):
        self.bits = bits
        self.twos = 0 # >=0, twos complenment of val
        self.val = 0  # integer representation

    def print(self):
        print(f"bits {self.bits}\nVAL {self.val}\nTWOS {self.twos}")

    def set_val(self, new_val):
        if not fits_in_bound(new_val, self.bits):
            print(f"[ERROR] {new_val} out of range of int{self.bits} = {get_int_bounds(self.bits)}", file=sys.stderr)

        self.val = new_val
        self.twos = int_to_twos(new_val, self.bits)

    def set_twos(self, new_twos):
        self.twos = new_twos
        self.val = twos_to_int(new_twos, self.bits)
    
    def inverse(self):
        self.set_val(self.val * (-1))


def convert_to_int64_list(num: Bimber, n: int):
    ret = []
    num = num.twos
    for i in range(n):
        ret.append(twos_to_int(num % (1<<64), 64))
        num = num >> 64
    return ret

def get_int_bounds(bits):
    return [ - 1<<(bits-1), (1<<(bits-1)) - 1 ]

def fits_in_bound(num, bits):
    val_range = get_int_bounds(bits)
    return num >= val_range[0] and num <= val_range[1]



class GenerateTest():
    def __init__(self, n, a_range=[0, (1<<64)-1], b_range=[0, (1<<64)-1]):
        self.n = n
        self.a_list = []
        self.a_range = a_range # [x,y] włącznie
        self.b_range = b_range # [x,y] 
        self.a = Bimber(n * 64)
        self.b  = Bimber(64)
        self.x = Bimber(n * 64)
        self.r = Bimber(64)
        self.x_list = []
        self.bits = n * 64
        self.msb = 1 << (n-1)

    def set_seed(self, _seed):
        seed(_seed)
    
    def generate_test_data(self):
        # in 64 bit chunks
        # It does not generate overflow test (there is a small chance it does but...)
        sum = 0
        for i in range(self.n):
            num = randint(self.a_range[0], self.a_range[1])      # generate unsigned
            sum += num * (1<<(64*i))

        self.a.set_twos(sum)
        self.b.set_twos(randint(self.b_range[0], self.b_range[1]))

    def print_debug(self):
        print("======== DEBUG ==========")
        print(self.n)
        # print("dividend: ", self.a_list)
        print(self.a.val,"/",self.b.val)
        print(self.x.val,"r", self.r.val)
        print("check: ", self.x.val*self.b.val + self.r.val == self.a.val)
        print("quotient: ", self.x_list)
        self.a.print()
        self.b.print()
        self.x.print()
        self.r.print()

    def print_test(self, id=0):
        print(id, self.n)
        # for dawg in self.a_list: #dzielna
        #     print(dawg,end=" ")
        print(' '.join(map(str, self.a_list)))
        print(f"{self.b.val}") #dzielnik

        # for dawg in self.x_list: #wynik 
        #     print(dawg, end=" ")
        print(' '.join(map(str, self.x_list)))
        print(f"{self.r.val}") #reszta
        
    def overflow(self):
        self.a.set_twos(1<<((self.bits-1)))
        self.b.set_val(-1)
        self.a_list = [0 for i in range(self.n)]
        self.a_list[-1] = -(1<<64) # INT64_MIN
        self.x_list = [0 for i in range(self.n)]
        # self.x_list = convert_to_int64_list(self.x, self.n)
        # self.a_list = convert_to_int64_list(self.a, self.n)

    # returns false if no overflow, true if overflow
    def calc_test(self):
        
        # overflow TEST GEN, MSchunk = INT_MIN, rest = 0
        if self.a_range == [1<<63, 1<<63] and self.b_range == [(1<<64)-1, (1<<64)-1]:
            self.overflow()
            return
        
        self.generate_test_data()
        
        # divide, generate_test SIGNED a and b
        # for signs to match "C" conventions
        self.x.set_val(abs(self.a.val) // abs(self.b.val))
        self.r.set_val(abs(self.a.val)  % abs(self.b.val))

        # 1-, 0+
        sign_a = self.a.val < 0
        sign_b = self.b.val < 0

        if sign_a ^ sign_b:
            self.x.inverse()
        if sign_a:
            self.r.inverse()


        # to jest bez sensu, nie chce mi się tego teraz usuwać
        fail = False
        if self.a.twos == ((1<<self.bits)-1) and self.b.val == -1:
            print("[OVERFLOW] MIN and -1",self.a.twos, self.b.val, self.bits, file=sys.stderr)
            fail = True

        # convert to int before  printing because mdiv_example wants int, not uint
        self.x_list = convert_to_int64_list(self.x, self.n)
        self.a_list = convert_to_int64_list(self.a, self.n)
        return fail

    def run_test(self, id=0):
        fail = self.calc_test()
        self.print_test(id)
        return fail

if __name__ == "__main__":
    print("========= BOUNDS =========")
    print(get_int_bounds(32), get_int_bounds(64))
    print("========= BOUNDS =========")
    t1 = GenerateTest(2, [0, (1<<64)-1], [2, 6])

    t1.calc_test()
    t1.print_test()
    t1.print_debug()

