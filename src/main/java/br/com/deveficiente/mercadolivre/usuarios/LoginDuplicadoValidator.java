package br.com.deveficiente.mercadolivre.usuarios;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class LoginDuplicadoValidator implements Validator {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean supports(Class<?> clazz) {
        // 1 ICP: NovoUsuarioRequest
        return NovoUsuarioRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //2 ICP: if/else
        if (errors.hasErrors()) {
            return;
        }

        NovoUsuarioRequest request = (NovoUsuarioRequest) target;
        String jpql = "SELECT u FROM Usuario u WHERE u.login = :login";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("login", request.login());

        //2 ICP: if/else
        //1 ICP: negação "!"
        if(!query.getResultList().isEmpty()) {
            errors.rejectValue("login",null, "já está cadastrado");
        }
    }
}
