package com.openclassrooms.realestatemanager.views.adapters

/**
 * Click interface for RecyclerView of [ListEstateAgentAddAdapter], [ListPhotoAddAdapter]
 */
interface OnItemClickEdit {

    fun onClickDeleteEstateAgent(position : Int)

    fun onClickEditPhoto(position : Int)

    fun onClickDeletePhoto(position : Int)
}