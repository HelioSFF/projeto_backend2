package com.projeto_backend.ClinMed.config;

import com.projeto_backend.ClinMed.domain.especialidade.entity.EspecialidadeEntity;
import com.projeto_backend.ClinMed.domain.especialidade.repository.EspecialidadeRepository;
import com.projeto_backend.ClinMed.domain.medico.entity.MedicoEntity;
import com.projeto_backend.ClinMed.domain.medico.repository.MedicoRepository;
import com.projeto_backend.ClinMed.domain.paciente.entity.PacienteEntity;
import com.projeto_backend.ClinMed.domain.paciente.enums.StatusPaciente;
import com.projeto_backend.ClinMed.domain.paciente.repository.PacienteRepository;
import com.projeto_backend.ClinMed.domain.usuario.entity.UsuarioEntity;
import com.projeto_backend.ClinMed.domain.usuario.enums.StatusUsuario;
import com.projeto_backend.ClinMed.domain.usuario.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final EspecialidadeRepository especialidadeRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;

    public DataInitializer(UsuarioRepository usuarioRepository,
                           EspecialidadeRepository especialidadeRepository,
                           MedicoRepository medicoRepository,
                           PacienteRepository pacienteRepository) {
        this.usuarioRepository = usuarioRepository;
        this.especialidadeRepository = especialidadeRepository;
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        if (especialidadeRepository.count() > 0) return;
        // 1. Seed Specialties
        EspecialidadeEntity cardiologia = new EspecialidadeEntity();
        cardiologia.setNome("Cardiologia");
        cardiologia.setDescricao("Especialidade médica que cuida do coração e do sistema circulatório.");
        cardiologia = especialidadeRepository.save(cardiologia);

        EspecialidadeEntity pediatria = new EspecialidadeEntity();
        pediatria.setNome("Pediatria");
        pediatria.setDescricao("Especialidade dedicada à saúde das crianças e adolescentes.");
        pediatria = especialidadeRepository.save(pediatria);

        EspecialidadeEntity oftalmologia = new EspecialidadeEntity();
        oftalmologia.setNome("Oftalmologia");
        oftalmologia.setDescricao("Especialidade que estuda e trata as doenças dos olhos.");
        oftalmologia = especialidadeRepository.save(oftalmologia);

        // 2. Seed Users
        UsuarioEntity userAdmin = new UsuarioEntity();
        userAdmin.setNome("Helio Admin");
        userAdmin.setEmail("admin@clinmed.com.br");
        userAdmin.setSenha("admin123");
        userAdmin.setStatus(StatusUsuario.ADMIN);
        usuarioRepository.save(userAdmin);

        UsuarioEntity userMedico1 = new UsuarioEntity();
        userMedico1.setNome("Dr. Carlos Alberto");
        userMedico1.setEmail("carlos@clinmed.com.br");
        userMedico1.setSenha("medico123");
        userMedico1.setStatus(StatusUsuario.MEDICO);
        userMedico1 = usuarioRepository.save(userMedico1);

        UsuarioEntity userMedico2 = new UsuarioEntity();
        userMedico2.setNome("Dra. Julia Souza");
        userMedico2.setEmail("julia@clinmed.com.br");
        userMedico2.setSenha("medico123");
        userMedico2.setStatus(StatusUsuario.MEDICO);
        userMedico2 = usuarioRepository.save(userMedico2);

        UsuarioEntity userRecepcionista = new UsuarioEntity();
        userRecepcionista.setNome("Ana Recepcionista");
        userRecepcionista.setEmail("ana@clinmed.com.br");
        userRecepcionista.setSenha("recep123");
        userRecepcionista.setStatus(StatusUsuario.RECEPCIONISTA);
        usuarioRepository.save(userRecepcionista);

        // 3. Seed Doctors
        MedicoEntity medico1 = new MedicoEntity();
        medico1.setUsuario(userMedico1);
        medico1.setEspecialidade(cardiologia);
        medico1.setDetalhes("Especialista em cardiologia intervencionista.");
        medicoRepository.save(medico1);

        MedicoEntity medico2 = new MedicoEntity();
        medico2.setUsuario(userMedico2);
        medico2.setEspecialidade(pediatria);
        medico2.setDetalhes("Especialista em pediatria neonatal.");
        medicoRepository.save(medico2);

        // 4. Seed Patients
        PacienteEntity pacienteAtivo = new PacienteEntity();
        pacienteAtivo.setNome("João da Silva");
        pacienteAtivo.setCpf("12345678901");
        pacienteAtivo.setDataNascimento(LocalDate.of(1990, 5, 10));
        pacienteAtivo.setEmail("joao@gmail.com");
        pacienteAtivo.setTelefone("11999999999");
        pacienteAtivo.setStatus(StatusPaciente.ATIVO);
        pacienteRepository.save(pacienteAtivo);

        PacienteEntity pacienteSuspenso = new PacienteEntity();
        pacienteSuspenso.setNome("Maria de Souza");
        pacienteSuspenso.setCpf("98765432109");
        pacienteSuspenso.setDataNascimento(LocalDate.of(1985, 12, 20));
        pacienteSuspenso.setEmail("maria@gmail.com");
        pacienteSuspenso.setTelefone("11988888888");
        pacienteSuspenso.setStatus(StatusPaciente.SUSPENSO);
        pacienteRepository.save(pacienteSuspenso);

    }
}
