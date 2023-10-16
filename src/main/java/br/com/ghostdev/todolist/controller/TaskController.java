package br.com.ghostdev.todolist.controller;

import br.com.ghostdev.todolist.repository.TaskRepository;
import br.com.ghostdev.todolist.task.TaskModel;
import br.com.ghostdev.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request){
//        System.out.println(request.getAttribute("idUser"));
        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID) idUser);

        var correntDate = LocalDateTime.now();
        if(correntDate.isAfter(taskModel.getStartAt()) || correntDate.isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio/termino deve ser maior que a data atual");
        }
        if(taskModel.getStartAt().isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio deve ser maior do que a data de termino");
        }

        return ResponseEntity.status(HttpStatus.OK).body(taskRepository.save(taskModel));
    }

    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request){
        var idUser = request.getAttribute("idUser");
        var tasks = this.taskRepository.findByIdUser((UUID) idUser);
        return tasks;
    }

    @PutMapping("/{id}")
    public ResponseEntity updateTasks(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id){

       var task = this.taskRepository.findById(id).orElse(null);

       if(task == null){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                   .body("Tarefa nao encontrada");
       }

       var idUser = request.getAttribute("idUser");

       if(!task.getIdUser().equals(idUser)){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                   .body("Usuario nao tem permissao para alterar esta tarefa");
       }

       Utils.copyNonNullProperties(taskModel, task);

       return ResponseEntity.ok().body(this.taskRepository.save(task));
    }
}
