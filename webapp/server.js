'use strict';

let compression = require('compression');
let express = require('express');
let logger = require('morgan');
let https = require('https');
let http = require('http');
let fs = require('fs');
let path = require('path');
let proxy = require('http-proxy-middleware');

let app = express();

app.set('port', process.env.PORT || 8080);
app.set('eap-service', process.env.EAP || 'http://eap-app:8080');
app.set('springboot-service', process.env.SPRINGBOOT || 'http://springboot-app:8080');

app.use(compression());

app.use(logger('combined'));

app.use(express.static(path.join(__dirname, 'dist')));

// proxy for jboss backend
app.use(
  '/jboss-api/*',
  proxy({
    target: app.get('eap-service'),
    secure: false,
    changeOrigin: true,
    logLevel: 'debug',
    pathRewrite: {
      '^/eap-service': ''
    }
  })
);

// proxy for springboot backend
app.use(
  '/springboot-api/*',
  proxy({
    target: app.get('springboot-service'),
    secure: false,
    changeOrigin: true,
    logLevel: 'debug',
    pathRewrite: {
      '^/springboot-service': ''
    }
  })
);

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
