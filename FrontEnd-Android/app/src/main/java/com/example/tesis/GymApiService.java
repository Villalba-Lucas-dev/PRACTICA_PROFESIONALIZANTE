package com.example.tesis;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GymApiService {

    // Le indicamos que es un metodo POST y la ruta del backend
    @POST("/api/auth/registro")
    Call<Void> registrarCliente(@Body Cliente nuevoCliente);

    @POST("/api/auth/login")
    Call<AuthResponse> loginCliente(@Body LoginRequest loginRequest);

    @GET("/api/admin/usuarios")
    Call<List<ClienteResponse>> obtenerUsuarios();

    @PUT("/api/admin/usuarios/{id}/rol")
    Call<Void> cambiarRol(@Path("id") Integer id, @Body CambioRolRequest request);

    @DELETE("/api/admin/usuarios/{id}")
    Call<Void> desactivarUsuario(@Path("id") Integer id);

    @PUT("/api/admin/usuarios/{id}/reactivar")
    Call<Void> reactivarUsuario(@Path("id") Integer id);

    @GET("/api/usuarios/{id}")
    Call<ClienteResponse> obtenerMiPerfil(@Path("id") Integer id);

    @PUT("/api/usuarios/{id}/perfil")
    Call<Void> actualizarMiPerfil(@Path("id") Integer id, @Body ActualizarPerfilRequest request);
}