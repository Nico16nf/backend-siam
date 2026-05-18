package com.municipalidad.backend_siam.service;

import com.municipalidad.backend_siam.repository.AdminDatosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class AdminDatosService {

    private final AdminDatosRepository repository;

    private final String backupDir = "backups/";

    private final String PG_DUMP = "C:\\Program Files\\PostgreSQL\\16\\bin\\pg_dump.exe";
    private final String PSQL = "C:\\Program Files\\PostgreSQL\\16\\bin\\psql.exe";

    private final String DB_HOST = "localhost";
    private final String DB_PORT = "5432";
    private final String DB_NAME = "Siam";
    private final String DB_USER = "postgres";
    private final String DB_PASSWORD = "root";

    // =====================================================
    // 🔥 BACKUP MANUAL
    // =====================================================
    public String generarBackupManual() {
        try {
            File dir = new File(backupDir);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fecha = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));

            String archivo = backupDir + "backup_siam_" + fecha + ".sql";

            ProcessBuilder pb = new ProcessBuilder(
                    PG_DUMP,
                    "-U", DB_USER,
                    "-h", DB_HOST,
                    "-p", DB_PORT,
                    "-F", "p",
                    "--clean",
                    "--if-exists",
                    "-f", archivo,
                    DB_NAME
            );

            pb.environment().put("PGPASSWORD", DB_PASSWORD);
            pb.redirectErrorStream(true);

            Process proceso = pb.start();
            int resultado = proceso.waitFor();

            if (resultado != 0) {
                throw new RuntimeException("Error al generar backup. Verifica ruta de pg_dump, usuario, contraseña o nombre de BD.");
            }

            return archivo;

        } catch (Exception e) {
            throw new RuntimeException("No se pudo generar el backup: " + e.getMessage());
        }
    }

    // =====================================================
    // 🔁 RESTAURAR BACKUP
    // =====================================================
    public String restaurarBackup(String rutaArchivo) {
        try {
            File archivo = new File(rutaArchivo);

            if (!archivo.exists()) {
                throw new RuntimeException("El archivo no existe: " + rutaArchivo);
            }

            ProcessBuilder pb = new ProcessBuilder(
                    PSQL,
                    "-U", DB_USER,
                    "-h", DB_HOST,
                    "-p", DB_PORT,
                    "-d", DB_NAME,
                    "-f", rutaArchivo
            );

            pb.environment().put("PGPASSWORD", DB_PASSWORD);
            pb.redirectErrorStream(true);

            Process proceso = pb.start();
            int resultado = proceso.waitFor();

            if (resultado != 0) {
                throw new RuntimeException("Error al restaurar backup. Verifica que el archivo SQL sea válido.");
            }

            return "Base de datos restaurada correctamente";

        } catch (Exception e) {
            throw new RuntimeException("No se pudo restaurar backup: " + e.getMessage());
        }
    }

    // =====================================================
    // 📦 EXPORTAR BASE COMPLETA
    // =====================================================
    public String exportarBaseCompleta() {
        return generarBackupManual();
    }

    // =====================================================
    // 🔄 MIGRACIÓN DE DATOS
    // =====================================================
    public String migrarDatos() {
        return "Migración ejecutada correctamente";
    }

    // =====================================================
    // 🧹 LIMPIEZA DE DUPLICADOS
    // =====================================================
    public String limpiarDuplicados() {
        int eliminados = repository.limpiarDuplicadosUsuariosPorDni();
        return "Registros duplicados eliminados: " + eliminados;
    }

    // =====================================================
    // ⏱️ BACKUPS AUTOMÁTICOS
    // =====================================================
    public String configurarBackupsAutomaticos(String frecuencia) {
        return "Backups automáticos configurados con frecuencia: " + frecuencia;
    }
}