(function() {
  define(["lib/backbone", "lib/underscore", "lib/jquery", "lib/less", "lib/handlebars"], function(Backbone, _, $, less, handlebars) {
    if (typeof console !== "undefined" && console !== null) {
      if (typeof console.log === "function") {
        console.log(Backbone);
      }
    }
    if (typeof console !== "undefined" && console !== null) {
      if (typeof console.log === "function") {
        console.log(_);
      }
    }
    if (typeof console !== "undefined" && console !== null) {
      if (typeof console.log === "function") {
        console.log($);
      }
    }
    if (typeof console !== "undefined" && console !== null) {
      if (typeof console.log === "function") {
        console.log(less);
      }
    }
    if (typeof console !== "undefined" && console !== null) {
      if (typeof console.log === "function") {
        console.log(handlebars);
      }
    }
    if (typeof console !== "undefined" && console !== null) {
      if (typeof console.log === "function") {
        console.log("Bracket controller");
      }
    }
    return document.write("<h4>Coffeescript's bracket controller says hello.</h4>");
  });
}).call(this);
