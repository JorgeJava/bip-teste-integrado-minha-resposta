import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BeneficioService } from '../services/beneficio.service';
import { Beneficio, Transferencia } from '../models/beneficio.model';

@Component({
  selector: 'app-beneficio-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './beneficio-list.component.html',
  styleUrl: './beneficio-list.component.css'
})
export class BeneficioListComponent implements OnInit {
  beneficios: Beneficio[] = [];
  selectedBeneficio: Beneficio | null = null;
  showModal = false;
  showTransferModal = false;
  isEditMode = false;
  errorMessage = '';
  successMessage = '';

  transferencia: Transferencia = {
    fromId: 0,
    toId: 0,
    amount: 0
  };

  beneficioForm: Beneficio = {
    nome: '',
    descricao: '',
    valor: 0,
    ativo: true
  };

  constructor(private beneficioService: BeneficioService) {}

  ngOnInit() {
    this.loadBeneficios();
  }

  loadBeneficios() {
    this.beneficioService.getAll().subscribe({
      next: (data) => {
        this.beneficios = data;
        this.clearMessages();
      },
      error: (error) => {
        this.errorMessage = 'Erro ao carregar benefícios: ' + error.message;
      }
    });
  }

  openCreateModal() {
    this.isEditMode = false;
    this.beneficioForm = {
      nome: '',
      descricao: '',
      valor: 0,
      ativo: true
    };
    this.showModal = true;
    this.clearMessages();
  }

  openEditModal(beneficio: Beneficio) {
    this.isEditMode = true;
    this.selectedBeneficio = beneficio;
    this.beneficioForm = { ...beneficio };
    this.showModal = true;
    this.clearMessages();
  }

  openTransferModal() {
    this.transferencia = {
      fromId: 0,
      toId: 0,
      amount: 0
    };
    this.showTransferModal = true;
    this.clearMessages();
  }

  closeModal() {
    this.showModal = false;
    this.showTransferModal = false;
    this.selectedBeneficio = null;
    this.clearMessages();
  }

  saveBeneficio() {
    if (this.isEditMode && this.selectedBeneficio?.id) {
      this.beneficioService.update(this.selectedBeneficio.id, this.beneficioForm).subscribe({
        next: () => {
          this.successMessage = 'Benefício atualizado com sucesso!';
          this.loadBeneficios();
          this.closeModal();
        },
        error: (error) => {
          this.errorMessage = 'Erro ao atualizar benefício: ' + error.message;
        }
      });
    } else {
      this.beneficioService.create(this.beneficioForm).subscribe({
        next: () => {
          this.successMessage = 'Benefício criado com sucesso!';
          this.loadBeneficios();
          this.closeModal();
        },
        error: (error) => {
          this.errorMessage = 'Erro ao criar benefício: ' + error.message;
        }
      });
    }
  }

  deleteBeneficio(id: number) {
    if (confirm('Tem certeza que deseja excluir este benefício?')) {
      this.beneficioService.delete(id).subscribe({
        next: () => {
          this.successMessage = 'Benefício excluído com sucesso!';
          this.loadBeneficios();
        },
        error: (error) => {
          this.errorMessage = 'Erro ao excluir benefício: ' + error.message;
        }
      });
    }
  }

  executeTransfer() {
    this.beneficioService.transfer(this.transferencia).subscribe({
      next: () => {
        this.successMessage = 'Transferência realizada com sucesso!';
        this.loadBeneficios();
        this.closeModal();
      },
      error: (error) => {
        this.errorMessage = 'Erro na transferência: ' + error.message;
      }
    });
  }

  clearMessages() {
    this.errorMessage = '';
    this.successMessage = '';
  }

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(value);
  }
}

