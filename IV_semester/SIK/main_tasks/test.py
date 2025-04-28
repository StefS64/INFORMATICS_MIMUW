import sys

if len(sys.argv) != 2:
    print(f'Usage: {sys.argv[0]} <peers>')

first_port = 1100
peers_count = int(sys.argv[1])



import threading
import subprocess
import time
import re

def color(v: int) -> str:
    code = 91 + (v % 6)
    return f'\033[{code}m'

rc = '\033[0m'
#erase = '\x1b[1A\x1b[2K'
#erase = '\x1b[1A\x1b[2K\x1b[G'
erase = '\x1b[2K\x1b[G'

start = time.time()


prompt = ""

def render_prompt():
    print(f"{erase}> {prompt}", end="", flush=True)

def render_log(msg: str):
    print(f"{erase}{msg}")
    render_prompt()

def comment(line: str):
    c = ''
    if '0x' in line:
        code = line.split('0x')[1]

        try:
            msg_type = int(code[0:2], 16)
            c += {
                1: 'HELLO',
                2: 'HELLO_REPLY',
                3: 'CONNECT',
                4: 'ACK_CONNECT',
                11: 'SYNC_START',
                12: 'DELAY_REQUEST',
                13: 'DELAY_RESPONSE',
                21: 'LEADER',
                31: 'GET_TIME',
                32: 'TIME',
            }[msg_type]
        except Exception:
            c += '!!! MALFORMED TYPE !!!'
            return c

        try:
            if msg_type in [21]:
                synchronized = int(code[2:4], 16)
                c += f' synchronized: {synchronized}'
            if msg_type in [11, 13, 32]:
                synchronized = int(code[2:4], 16)
                timestamp = int(code[4:20], 16)
                c += f' synchronized: {synchronized} timestamp: {timestamp}'
            if msg_type in [2]:
                count = int(code[2:6], 16)
                c += f' count: {count}'
        except Exception:
            c += ' !!! MALFORMED DATA !!!'
            return c

    return c


class PeerTimeSync(threading.Thread):
    def __init__(self, port: int, peer_addr: str = None, peer_port: int = None):
        self.port = port
        self.peer_addr = peer_addr
        self.peer_port = peer_port
        self.proc: Popen = None

        threading.Thread.__init__(self)

    def run(self):
        args = ['./peer-time-sync', '-p', str(self.port)]

        if self.peer_addr is not None:
            args.append('-a')
            args.append(self.peer_addr)
        if self.peer_port is not None:
            args.append('-r')
            args.append(str(self.peer_port))

        self.proc = subprocess.Popen(
            args,
            shell=False,
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            stdin=subprocess.PIPE,
            text=True,
        )

        cc = color(self.port)
        def log(msg: str):
            render_log(f"{cc}{self.port}\t{time.time() - start:.3f}\t{msg}{rc} {comment(msg)}")

        log('BEGIN')
        while line := self.proc.stdout.readline():
            log(line.strip('\n'))
        self.proc.wait()
        log(f"END {self.proc.returncode}")



### MAIN ###

import random
import signal
import socket
import termios
import tty

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

def listen(port: int):
    sock.bind(('0.0.0.0', port))
    while True:
        data, addr = sock.recvfrom(4096) # buffer size is 1024 bytes
        string = data.hex()
        render_log(f"{addr} -> {string} {comment('0x' + string)}")

peers: list[PeerTimeSync] = []

def send(peer_index: int, data: bytes):
    sock.sendto(data, ('127.0.0.1', peers[peer_index].port))

def cmd_leader(args):
    peer_index, value = args
    send(int(peer_index), bytes([21, int(value)]))

def cmd_time(args):
    if args:
        peer_index, = args
        send(int(peer_index), bytes([31]))
    else:
        for peer_index, _ in enumerate(peers):
            send(int(peer_index), bytes([31]))

def cmd_send(args):
    peer_index, *vals = args
    send(int(peer_index), bytes([int(val) for val in vals]))

def cmd_new(args):
    count = 1
    if args:
        count, = args
        count = int(count)

    for _ in range(count):
        if peers:
            port = random.choice([peer.port for peer in peers])
            peer = PeerTimeSync(first_port + len(peers), '127.0.0.1', port)
        else:
            peer = PeerTimeSync(first_port)
        peers.append(peer)
        peer.start()
        time.sleep(0.2)

commands = {
    'leader': cmd_leader,
    'time': cmd_time,
    'send': cmd_send,
    'new': cmd_new,
}

def execute(cmd: str):
    cmd = cmd.strip()
    while '  ' in cmd:
        cmd = cmd.replace('  ', ' ')
    cmd, *args = cmd.split(' ')
    if cmd in commands:
        render_log(f"command: {cmd} {args}")
        try:
            commands[cmd](args)
        except Exception as e:
            render_log(e)
    else:
        render_log(f"invalid command: {cmd} {args}")

def suggest(cmd: str):
    global prompt
    matching = {c for c in commands if c.startswith(cmd)}
    if len(matching) == 1:
        prompt = matching.pop() + ' '
    else:
        first = matching.pop()
        matching.add(first)

        while not all([m.startswith(first) for m in matching]):
            first = first[:-1]

        if prompt == first:
            render_log(' '.join(matching))
        else:
            prompt = first



if __name__ == "__main__":
    in_fd = sys.stdin.fileno()
    original = termios.tcgetattr(in_fd)

    listener = threading.Thread(target=listen, args=(2200,))
    listener.start()

    try:
        tty.setcbreak(in_fd, tty.TCSANOW)

        for i in range(peers_count):
            if peers:
                port = random.choice([peer.port for peer in peers])
                peer = PeerTimeSync(first_port+i, '127.0.0.1', port)
            else:
                peer = PeerTimeSync(first_port)
            peers.append(peer)
            peer.start()
            time.sleep(0.1)

        while True:
            char = sys.stdin.read(1)
            match char:
                case '\x7f':
                    prompt = prompt[:-1]
                case '\n':
                    execute(prompt)
                    prompt = ""
                case '\t':  # TAB
                    suggest(prompt)
                case _:
                    if char.lower() in 'abcdefghijklmnopqrstuvwxyz 0123456789':
                        prompt += char
                    else:
                        render_log(f"pressed {ord(char)}")
            render_prompt()
    except KeyboardInterrupt:
        render_log("sending interrupt to all peers")
        for peer in peers:
            peer.proc.send_signal(signal.SIGINT)
    except Exception as e:
        render_log(e)
    finally:
        termios.tcsetattr(in_fd, termios.TCSANOW, original)

        for peer in peers:
            peer.join()
        print()
        exit(0)
        #listener.join()
