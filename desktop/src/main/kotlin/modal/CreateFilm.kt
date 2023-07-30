package modal

import kotlinx.serialization.Serializable

@Serializable
data class CreateFilm(val title:String,val director:String,val year:Int,val poster:String)
