package com.example.leiaaqui.Servicos;
import com.example.leiaaqui.Model.ClienteModel;
import com.example.leiaaqui.Model.LivroModel;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ServicosInterface {

    @GET("Livros")
    Call<List<LivroModel>> getLivros();

    @GET("Cliente")
    Call<List<ClienteModel>> getClientes();

    @POST("Cliente")
    Call<ClienteModel> saveCliente(@Body String body);

    @GET("categoriaLeitores")
    Call<List<LivroModel>> getCategoriaLeitores();

    @GET("categoriaLivros")
    Call<List<LivroModel>> getCategoriaLivros();
}
