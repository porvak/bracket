function (poolId, round) {
    return db.pool.findOne({_id:ObjectId(poolId)}).scoringStrategy[round];
}