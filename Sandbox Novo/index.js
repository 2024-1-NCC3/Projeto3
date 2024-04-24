// console.log("Hello CodeSandbox");
var express = require("express");
var app = express();
var bodyParser = require("body-parser");
var PORT = process.env.PORT || 3000;
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));
// banco de dados
var sqlite3 = require("sqlite3").verbose();
var DBPATH = "projeto3bd.db";
var db = new sqlite3.Database(DBPATH);

//  teste 2
app.get("/tudo", (req, res) => {
  db.all("SELECT * FROM curso", (err, rows) => {
    if (err) {
      res.status(500).json({ error: err.message });
      return;
    }
    res.json(rows);
  });
});
// https://g5d9cp-3000.csb.app
//teste1
// app.get("/tudo", function (req, res) {
//   db.all(`SELECT * FROM curso`, [], (err, rows) => {
//     if (err) {
//       res.send(err);
//     } else {
//       res.send(rows);
//     }
//   });
// });

// iniciar o servidor
app.listen(PORT, () => {
  console.log(`Servidor Express rodando na porta ${PORT}`);
});
