package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity addPaciente(@RequestBody @Valid DadosDoPaciente dados, UriComponentsBuilder uriBuilder) {
        var newPaciente = new Paciente(dados);
        repository.save(newPaciente);

        var uri =  uriBuilder.path("/pacientes/{id}").buildAndExpand(newPaciente.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoPaciente(newPaciente));

    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemPaciente>> getPacientes(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemPaciente::new);

        return ResponseEntity.ok(page);
    }

  @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoPaciente> getOnePaciente(@PathVariable Long id) {
        var pacienteToFind = repository.getReferenceById(id);

        return ResponseEntity.ok(new DadosDetalhamentoPaciente(pacienteToFind));
    }

    @PutMapping
    @Transactional
    public ResponseEntity updatePaciente(@RequestBody @Valid DadosAtualizacaoPaciente dados) {
        var paciente = repository.getReferenceById(dados.id());
        paciente.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deletePaciente(@PathVariable Long id) {
        var paciente = repository.getReferenceById(id);
        paciente.softDelete();

        return ResponseEntity.noContent().build();

    }

}
