define [
  'base/jsonUri'
  'lib/jquery'
  'app/model/TournamentModel'
  'app/view/TournamentView'
], (jsonUri, $,TournamentModel,TournamentView) ->
  {
    construct:->
      @contentNode = $("#bracketNode")
      @model = new TournamentModel
      @view = new TournamentView(model:@model)
      @model.fetch()

    init:->
      @construct()

  }