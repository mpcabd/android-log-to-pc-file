# This work is licensed under the GNU Public License (GPL).
# To view a copy of this license, visit http://www.gnu.org/copyleft/gpl.html

# Written by Abd Allah Diab (mpcabd)
# Email: mpcabd ^at^ gmail ^dot^ com
# Website: http://mpcabd.igeex.biz

from BaseHTTPServer import HTTPServer, BaseHTTPRequestHandler

import threading
import cgi
import time

IP = '192.168.1.3'
PORT = 5500
 
LOG_FILE = r'C:\AndroidLogFile.log'
 
POST_PARAMETER = 'log'

class LogHandler(BaseHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        BaseHTTPRequestHandler.__init__(self, *args, **kwargs)
        
    def do_POST(self):
        length = int(self.headers.getheader('content-length'))
        postvars = cgi.parse_qs(self.rfile.read(length), keep_blank_values=1)
        f = open(LOG_FILE, 'a')
        f.write('%s:\n%s\n%s\n' % (time.strftime('%Y/%m/%d %H:%M:%S'), postvars[POST_PARAMETER][0], '*' * 50))
        f.close()
        self.send_response(200)
        self.send_header('Content-Type', 'text/plain')
        self.send_header('Cache-Control', 'no-cache')
        self.end_headers()
        self.wfile.write('Saved!')

def startThread(fn):
    thread = threading.Thread(target=fn)
    thread.setDaemon(True)
    thread.start()
    return thread
    
if __name__ == '__main__':
    httpd = HTTPServer((IP PORT), lambda *args, **kwargs: LogHandler(*args, **kwargs))
    serve_thread  = startThread(httpd.serve_forever)
    while serve_thread.isAlive():
        serve_thread.join(timeout=1)