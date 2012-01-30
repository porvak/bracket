define [
  "lib/backbone",
  "lib/underscore",
  "lib/jquery",
  "lib/less",
  "lib/handlebars"
], (Backbone,_,$,less,handlebars) ->
  console?.log? Backbone
  console?.log? _
  console?.log? $
  console?.log? less
  console?.log? handlebars
  console?.log? "Bracket controller"
  document.write "<h4>Coffeescript's bracket controller says hello.</h4>"

