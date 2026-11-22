const http = require('http');

const server = http.createServer((req, res) => {
    let body = '';
    req.on('data', chunk => { body += chunk; });
    req.on('end', () => {
        // ЛОГИРУЕМ КАЖДЫЙ ЗАПРОС
        console.log('--- NEW REQUEST ---');
        console.log('RECEIVED BODY:', body);

        try {
            const parsed = JSON.parse(body);
            const rawNumber = parsed.number || '';
            const cleanNumber = rawNumber.replace(/\s/g, '');

            console.log('RAW CARD:', rawNumber);
            console.log('CLEAN CARD:', cleanNumber);

            let status = "APPROVED";
            if (cleanNumber === '5555666677778888') {
                status = "DECLINED";
            }

            console.log('DECISION:', status);

            res.writeHead(200, {'Content-Type': 'application/json'});
            res.end(JSON.stringify({status: status}));
        } catch (e) {
            console.log('PARSE ERROR:', e.message);
            res.writeHead(400);
            res.end();
        }
    });
});

server.listen(9999, () => console.log('Simulator running on 9999'));