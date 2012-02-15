define [
  'base/jsonUri'
  'lib/jquery'
  'app/model/TournamentModel'
  'app/view/TournamentView'
], (jsonUri, $,TournamentModel,TournamentView) ->
  {
    construct:->
      @model = new TournamentModel
      @view = new TournamentView(model:@model)

    init:->
      @construct()
      @model.fetch()


  }