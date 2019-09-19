'use strict';

let compression = require('compression');
let express = require('express');
let logger = require('morgan');
let https = require('https');
let http = require('http');
let fs = require('fs');
let path = require('path');
let proxy = require('http-proxy-middleware');
let cors = require('cors');

let app = express();

app.use(cors());
app.set('port', process.env.PORT || 8080);
app.set('microservices-apis', process.env.integration_uri || 'http://auth-integration-api/v1/:8080');
app.set('microservices-health-api', process.env.integration_health_uri || 'http://auth-integration-api/v1/:8081');

app.use(compression());
app.use(logger('combined'));
app.use(express.static(path.join(__dirname, 'dist')));

// proxy for auth-integration-api backend
/*
app.use(
  '/api/v1/*',
  proxy({
    target: app.get('microservices-apis'),
    secure: false,
    changeOrigin: true,
    logLevel: 'debug',
    pathRewrite: {
      '^/microservices-apis': ''
    }
  })
);

// proxy for auth-integration-health-api backend
app.use(
    '/health',
    proxy({
        target: app.get('microservices-health-api'),
        secure: false,
        changeOrigin: true,
        logLevel: 'debug',
        pathRewrite: {
            '^/microservices-health-api': ''
        }
    })
);
*/

app.use((req, res) => {

  // respond with index to process links
  if (req.accepts('html')) {
    res.sendFile(__dirname + '/dist/index.html');
    return;
  }

  // otherwise resource was not found
  res.status(404);
  if (req.accepts('json')) {
    res.send({error: 'Not found'});
    return;
  }

  res.type('txt').send('Not found');
});

const certConfig = {
  key: fs.readFileSync('server.key'),
  cert: fs.readFileSync(('server.cert'))
};

// for local ssl
if(app.get('port') !== 8080) {
  https.createServer(certConfig, app)
    .listen(app.get('port'), () => {
      console.log('Express secure server listening on port ' + app.get('port'));
    });
} else {
  // on openshift let route control ssl
  http.createServer(app)
    .listen(app.get('port'), () => {
      console.log('Express server listening on port ' + app.get('port'));
    });
}
