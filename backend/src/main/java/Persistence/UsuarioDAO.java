package Persistence;

import gradle.cucumber.Usuario;
import org.bson.types.ObjectId;

import java.util.List;

public class UsuarioDAO extends GenericMongoDAO<Usuario> {

    public UsuarioDAO() {
        super(Usuario.class);
    }

    public Usuario getUsuarioPorNombre(String usuarioNombre) {

        return this.find("{ nombreUsuario: # }", usuarioNombre).get(0);
    }

    public boolean existeUsuario(String usuarioNombre){
        return ! this.find("{ nombreUsuario: # }", usuarioNombre).isEmpty();
    }

    public boolean verificarUsuarioValido(String usuarioNombre, String password) {
        return this.find("{ nombreUsuario: # , password: #}", usuarioNombre , password).size() == 1;
    }

    public boolean existeUsuarioId(String idUsuario) {
        ObjectId idMongo = new ObjectId(idUsuario);
        return ! this.find("{ _id: # }", idMongo).isEmpty();
    }

    public void updateNotificacionesDeUsuarios(List<Usuario> usuarios) {
        usuarios.stream().forEach(user -> this.updateNotificaciones(user));
    }

    private void updateNotificaciones(Usuario user) {
            ObjectId objectId = new ObjectId(user.getId());
            this.mongoCollection.update("{ _id: # }", objectId).with("{ $set: { notificaciones: # }}", user.getNotificaciones());
    }
}
