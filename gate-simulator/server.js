const http = require('http');

const server = http.createServer((req, res) => {
    let body = '';
    req.on('data', chunk => { body += chunk; });
    req.on('end', () => {
        try {
            const parsed = JSON.parse(body);
            // Забираем номер карты из JSON
            const cardNumber = parsed.number || "";

            // Если это карта на отказ (без учета пробелов) — отказываем
            if (cardNumber.replace(/\s/g, '') === '5555666677778888') {
                res.writeHead(200, {'Content-Type': 'application/json'});
                res.end(JSON.stringify({status: "DECLINED"}));
            } else {
                // Во всех остальных случаях одобряем
                res.writeHead(200, {'Content-Type': 'application/json'});
                res.end(JSON.stringify({status: "APPROVED"}));
            }
        } catch (e) {
            console.error('Error parsing JSON:', e);
            res.writeHead(400);
            res.end();
        }
    });
});

server.listen(9999, () => console.log('Simulator running on 9999'));