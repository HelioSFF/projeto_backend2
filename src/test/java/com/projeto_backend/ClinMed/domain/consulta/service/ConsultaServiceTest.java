package com.projeto_backend.ClinMed.domain.consulta.service;

import com.projeto_backend.ClinMed.domain.consulta.entity.ConsultaEntity;
import com.projeto_backend.ClinMed.domain.consulta.enums.StatusConsulta;
import com.projeto_backend.ClinMed.domain.consulta.repository.ConsultaRepository;
import com.projeto_backend.ClinMed.domain.medico.entity.MedicoEntity;
import com.projeto_backend.ClinMed.domain.medico.service.MedicoService;
import com.projeto_backend.ClinMed.domain.paciente.entity.PacienteEntity;
import com.projeto_backend.ClinMed.domain.paciente.enums.StatusPaciente;
import com.projeto_backend.ClinMed.domain.paciente.service.PacienteService;
import com.projeto_backend.ClinMed.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ConsultaServiceTest {

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private PacienteService pacienteService;

    @Mock
    private MedicoService medicoService;

    @InjectMocks
    private ConsultaService consultaService;

    private PacienteEntity pacienteAtivo;
    private PacienteEntity pacienteSuspenso;
    private MedicoEntity medico;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pacienteAtivo = new PacienteEntity();
        pacienteAtivo.setId(1L);
        pacienteAtivo.setNome("João");
        pacienteAtivo.setStatus(StatusPaciente.ATIVO);

        pacienteSuspenso = new PacienteEntity();
        pacienteSuspenso.setId(2L);
        pacienteSuspenso.setNome("Maria");
        pacienteSuspenso.setStatus(StatusPaciente.SUSPENSO);

        medico = new MedicoEntity();
        medico.setId(1L);
    }

    @Test
    void agendar_DeveAgendarComSucesso() {
        // Arrange
        LocalDateTime dataHora = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        
        ConsultaEntity request = new ConsultaEntity();
        request.setPaciente(pacienteAtivo);
        request.setMedico(medico);
        request.setDataHoraInicio(dataHora);
        request.setMotivo("Rotina");

        when(pacienteService.buscarPorId(1L)).thenReturn(pacienteAtivo);
        when(medicoService.buscarPorId(1L)).thenReturn(medico);
        when(consultaRepository.existsOverlappingForMedico(any(), any(), any())).thenReturn(false);
        when(consultaRepository.existsOverlappingForPaciente(any(), any(), any())).thenReturn(false);
        when(consultaRepository.save(any(ConsultaEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ConsultaEntity response = consultaService.agendar(request);

        // Assert
        assertNotNull(response);
        assertEquals(StatusConsulta.AGENDADA, response.getStatus());
        assertEquals(dataHora, response.getDataHoraInicio());
        assertEquals(dataHora.plusMinutes(30), response.getDataHoraFim());
    }

    @Test
    void agendar_DeveLancarExcecao_QuandoTempoDeAntecedenciaForMenorQue30Minutos() {
        // Arrange
        LocalDateTime dataHoraInvalida = LocalDateTime.now().plusMinutes(10);
        ConsultaEntity request = new ConsultaEntity();
        request.setPaciente(pacienteAtivo);
        request.setMedico(medico);
        request.setDataHoraInicio(dataHoraInvalida);

        when(pacienteService.buscarPorId(1L)).thenReturn(pacienteAtivo);
        when(medicoService.buscarPorId(1L)).thenReturn(medico);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> consultaService.agendar(request));
        assertTrue(exception.getMessage().contains("antecedencia minima de 30 minutos"));
    }

    @Test
    void agendar_DeveLancarExcecao_QuandoHorarioForForaDeFuncionamento() {
        // Arrange
        LocalDateTime dataHoraInvalida = LocalDateTime.now().plusDays(1).withHour(21).withMinute(0);
        ConsultaEntity request = new ConsultaEntity();
        request.setPaciente(pacienteAtivo);
        request.setMedico(medico);
        request.setDataHoraInicio(dataHoraInvalida);

        when(pacienteService.buscarPorId(1L)).thenReturn(pacienteAtivo);
        when(medicoService.buscarPorId(1L)).thenReturn(medico);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> consultaService.agendar(request));
        assertTrue(exception.getMessage().contains("horario da consulta deve ser das 07:00 as 19:00"));
    }

    @Test
    void agendar_DeveLancarExcecao_QuandoPacienteForSuspenso() {
        // Arrange
        LocalDateTime dataHora = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        ConsultaEntity request = new ConsultaEntity();
        request.setPaciente(pacienteSuspenso);
        request.setMedico(medico);
        request.setDataHoraInicio(dataHora);

        when(pacienteService.buscarPorId(2L)).thenReturn(pacienteSuspenso);
        when(medicoService.buscarPorId(1L)).thenReturn(medico);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> consultaService.agendar(request));
        assertTrue(exception.getMessage().contains("paciente SUSPENSO"));
    }

    @Test
    void agendar_DeveLancarExcecao_QuandoMedicoTiverConflitoDeHorario() {
        // Arrange
        LocalDateTime dataHora = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        ConsultaEntity request = new ConsultaEntity();
        request.setPaciente(pacienteAtivo);
        request.setMedico(medico);
        request.setDataHoraInicio(dataHora);

        when(pacienteService.buscarPorId(1L)).thenReturn(pacienteAtivo);
        when(medicoService.buscarPorId(1L)).thenReturn(medico);
        when(consultaRepository.existsOverlappingForMedico(eq(1L), any(), any())).thenReturn(true);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> consultaService.agendar(request));
        assertTrue(exception.getMessage().contains("medico selecionado ja possui outra consulta"));
    }

    @Test
    void agendar_DeveLancarExcecao_QuandoPacienteTiverConflitoDeHorario() {
        // Arrange
        LocalDateTime dataHora = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        ConsultaEntity request = new ConsultaEntity();
        request.setPaciente(pacienteAtivo);
        request.setMedico(medico);
        request.setDataHoraInicio(dataHora);

        when(pacienteService.buscarPorId(1L)).thenReturn(pacienteAtivo);
        when(medicoService.buscarPorId(1L)).thenReturn(medico);
        when(consultaRepository.existsOverlappingForMedico(eq(1L), any(), any())).thenReturn(false);
        when(consultaRepository.existsOverlappingForPaciente(eq(1L), any(), any())).thenReturn(true);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> consultaService.agendar(request));
        assertTrue(exception.getMessage().contains("paciente ja possui outra consulta"));
    }

    @Test
    void confirmar_DeveConfirmarComSucesso() {
        // Arrange
        ConsultaEntity consulta = new ConsultaEntity();
        consulta.setId(10L);
        consulta.setStatus(StatusConsulta.AGENDADA);

        when(consultaRepository.findById(10L)).thenReturn(Optional.of(consulta));
        when(consultaRepository.save(any(ConsultaEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ConsultaEntity response = consultaService.confirmar(10L);

        // Assert
        assertEquals(StatusConsulta.CONFIRMADA, response.getStatus());
    }

    @Test
    void confirmar_DeveLancarExcecao_QuandoStatusNaoForAgendada() {
        // Arrange
        ConsultaEntity consulta = new ConsultaEntity();
        consulta.setId(10L);
        consulta.setStatus(StatusConsulta.REALIZADA);

        when(consultaRepository.findById(10L)).thenReturn(Optional.of(consulta));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> consultaService.confirmar(10L));
        assertTrue(exception.getMessage().contains("Somente consultas com status AGENDADA podem ser confirmadas"));
    }

    @Test
    void cancelar_DeveCancelarComSucesso_QuandoAntecedenciaForMaiorQue24Horas() {
        // Arrange
        ConsultaEntity consulta = new ConsultaEntity();
        consulta.setId(10L);
        consulta.setStatus(StatusConsulta.AGENDADA);
        consulta.setDataHoraInicio(LocalDateTime.now().plusDays(2));

        when(consultaRepository.findById(10L)).thenReturn(Optional.of(consulta));
        when(consultaRepository.save(any(ConsultaEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ConsultaEntity response = consultaService.cancelar(10L, "Imprevisto pessoal");

        // Assert
        assertEquals(StatusConsulta.CANCELADA, response.getStatus());
        assertEquals("Imprevisto pessoal", response.getMotivo());
    }

    @Test
    void cancelar_DeveLancarExcecao_QuandoMotivoNaoForInformado() {
        // Arrange
        ConsultaEntity consulta = new ConsultaEntity();
        consulta.setId(10L);
        consulta.setStatus(StatusConsulta.AGENDADA);
        consulta.setDataHoraInicio(LocalDateTime.now().plusDays(2));

        when(consultaRepository.findById(10L)).thenReturn(Optional.of(consulta));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> consultaService.cancelar(10L, ""));
        assertTrue(exception.getMessage().contains("motivo do cancelamento"));
    }

    @Test
    void cancelar_DeveLancarExcecao_QuandoAntecedenciaForMenorQue24Horas() {
        // Arrange
        ConsultaEntity consulta = new ConsultaEntity();
        consulta.setId(10L);
        consulta.setStatus(StatusConsulta.AGENDADA);
        consulta.setDataHoraInicio(LocalDateTime.now().plusHours(5));

        when(consultaRepository.findById(10L)).thenReturn(Optional.of(consulta));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> consultaService.cancelar(10L, "Imprevisto"));
        assertTrue(exception.getMessage().contains("antecedencia minima de 24 horas"));
    }
}
