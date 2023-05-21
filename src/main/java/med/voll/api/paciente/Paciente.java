package med.voll.api.paciente;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.endereco.Endereco;

@Table(name = "pacientes")
@Entity(name = "pacientes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String email;

    private String telefone;

    private String cpf;

    private boolean ativo;

    @Embedded
    private Endereco endereco;

    public Paciente(DadosDoPaciente dados) {
        this.ativo = true;
        this.nome = dados.nome();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.endereco = new Endereco(dados.endereco());
        this.cpf = dados.cpf();
    }

    public void atualizarInformacoes(DadosAtualizacaoPaciente dados) {
        if(dados.nome() != null) this.nome = dados.nome();
        if(dados.telefone() != null) this.email = dados.telefone();
        if(dados.endereco() != null) this.endereco.atualizarInformacoes(dados.endereco());
    }

    public void softDelete() {
        this.ativo = false;
    }
}
