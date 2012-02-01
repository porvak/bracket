define [
  'lib/backbone'
  'base/jsonUri'
  'lib/jquery'
], (Backbone, jsonUri, $) ->
  {
    construct:->
      @contentNode = $("#content")
      @collection = new Backbone.Collection
      @collection.url = jsonUri.teams

    init:->
      @construct()

      @contentNode?.html("<h4>Coffeescript's bracket controller says hello.</h4>")
      @collection?.fetch
          url:@url
          success:=>
            @writeBracket()
          error:=>
            @fetchFail()

    writeBracket:->
      @collection.each((team, i) => @contentNode?.append("<h5>" + team?.get("teamName") + "</h5>"))

    fetchFail:->
      window.console?.log "Fetch failed on #{@uri}"
  }