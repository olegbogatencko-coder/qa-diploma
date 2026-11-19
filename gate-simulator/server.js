const http = require('http');

const server = http.createServer((req, res) => {
    let body = '';
    req.on('data', chunk => { body += chunk; });
    req.on('end', () => {
        try {
            // Если в запросе есть номер карты 1111... возвращаем успех, иначе отказ
            if (body.includes('1111 2222 3333 4444')) {
                res.writeHead(200, {'Content-Type': 'application/json'});
                res.end(JSON.stringify({ status: "APPROVED" }));
            } else {
                res.writeHead(200, {'Content-Type': 'application/json'});
                res.end(JSON.stringify({ status: "DECLINED" }));
            }
        } catch (e) {
            res.writeHead(500);
            res.end();
        }
    });
});

server.listen(9999, () => {
    console.log('Simulator running on port 9999');
});