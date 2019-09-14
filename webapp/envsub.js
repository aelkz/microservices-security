'use strict';

const envsub = require('envsub');

const path = `${__dirname}/dist/assets/js`;
const templateFile = `${path}/env.template.js`;
const outputFile = `${path}/env.js`;
const options = {};

envsub({templateFile, outputFile, options}).then((envobj) => {
  // output file created
  console.log(envobj.templateFile);
  console.log(envobj.templateContents);
  console.log(envobj.outputFile);
  console.log(envobj.outputContents);
}).catch((err) => {
  console.error(err.message);
});
