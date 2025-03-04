package topq;

import retrofit2.Call;
import retrofit2.http.*;

public interface PetStoreService {

    @GET("pet/{id}")
    @Headers({
            "accept: application/json"
    })

    Call<String> getPetById(@Path("id") String id);

    @POST("pet")
    @Headers({
            "Content-Type: application/json"
    })
    Call<String> addPet(@Body String pet);


    @GET("store/order")
    Call<String> getStoreOrder();

    @GET("store/inventory")
    Call<String> getStoreInventory();



}
