package job.pim7;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.PixelGrabber;
import javax.swing.JPanel;

public class PIM7 extends JFrame {
    private JButton openButton;
    private JLabel imageLabel;
    private ImageIcon icon; // Agregar esta línea

    public PIM7() {
        // Configurar la ventana principal
        setTitle("PIM7");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear el botón "Abrir"
        openButton = new JButton("Abrir");
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Crear un objeto JFileChooser
                JFileChooser fileChooser = new JFileChooser();

                // Agregar un filtro para solo permitir archivos de imagen
                FileFilter imageFilter = new FileNameExtensionFilter("Archivos de imagen", "jpg", "jpeg", "png", "gif");
                fileChooser.setFileFilter(imageFilter);

                // Mostrar el cuadro de diálogo "Abrir archivo"
                int returnValue = fileChooser.showOpenDialog(null);

                // Comprobar si se seleccionó un archivo
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    System.out.println("Archivo seleccionado: " + selectedFile.getAbsolutePath());

                    // Mostrar la imagen en la ventana
                    displayImage(selectedFile);
                }
            }
        });

        // Crear el JLabel para mostrar la imagen
        imageLabel = new JLabel();
        // Mostrar la ventana
        setVisible(true);

        // Crear el botón "PixelGrabber"
        JButton pixelGrabberButton = new JButton("PixelGrabber");
        pixelGrabberButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (icon != null) {
                    processPixelGrabber();
                } else {
                    JOptionPane.showMessageDialog(PIM7.this, "No se ha seleccionado ninguna imagen.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Crear el botón "Aumentar RGB"
        JButton increaseButton = new JButton("Aumentar RGB");
        increaseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                increaseRGB();
            }
        });

        // Crear el botón "Disminuir RGB"
        JButton decreaseButton = new JButton("Disminuir RGB");
        decreaseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                decreaseRGB();
            }
        });
        
        // Crear el botón "Negativo"
        JButton negative = new JButton("Modo negativo");
        increaseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                applyNegative();
            }          
        });
        
        JButton saveButton = new JButton("Guardar");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveImage();
            }
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(openButton);
        buttonPanel.add(pixelGrabberButton);
        buttonPanel.add(increaseButton);
        buttonPanel.add(decreaseButton);
        buttonPanel.add(negative);
        buttonPanel.add(saveButton);

        // Crear el panel para la imagen
        JPanel imagePanel = new JPanel();
        imagePanel.add(imageLabel);

        // Crear el panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(imagePanel, BorderLayout.CENTER);

        // Agregar el panel principal a la ventana
        getContentPane().add(mainPanel);

        // Mostrar la ventana
        setVisible(true);
    }

    private void displayImage(File file) {
        try {
            // Leer la imagen desde el archivo seleccionado
            BufferedImage image = ImageIO.read(file);

            // Redimensionar la imagen para ajustarla al tamaño del JLabel (500x500)
            Image resizedImage = image.getScaledInstance(500, 500, Image.SCALE_SMOOTH);

            // Crear el ImageIcon con la imagen redimensionada
            icon = new ImageIcon(resizedImage);

            // Mostrar la imagen en el JLabel
            imageLabel.setIcon(icon);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar la imagen.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processPixelGrabber() {
        // Obtener la imagen del JLabel
        ImageIcon icon = (ImageIcon) imageLabel.getIcon();
        Image image = icon.getImage();

        // Obtener las dimensiones de la imagen
        int width = image.getWidth(null);
        int height = image.getHeight(null);

        // Crear un arreglo para almacenar los píxeles
        int[] pixels = new int[width * height];

        // Crear el objeto PixelGrabber
        PixelGrabber pixelGrabber = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);

        try {
            // Realizar la operación de PixelGrabber
            pixelGrabber.grabPixels();

            // Crear una nueva ventana para mostrar el gráfico
            JFrame graphFrame = new JFrame("Gráfico RGB");
            graphFrame.setSize(500, 400);
            graphFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            graphFrame.setLocationRelativeTo(null);

            // Crear un panel personalizado para dibujar el gráfico
            JPanel graphPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);

                    int barWidth = 100;
                    int barSpacing = 50;
                    int graphX = 50;
                    int graphY = getHeight() - 50;

                    int sumRed = 0;
                    int sumGreen = 0;
                    int sumBlue = 0;

                    // Procesar los píxeles obtenidos
                    for (int i = 0; i < height; i++) {
                        for (int j = 0; j < width; j++) {
                            int pixel = pixels[i * width + j];

                            int red = (pixel >> 16) & 0xFF;     // Componente rojo
                            int green = (pixel >> 8) & 0xFF;    // Componente verde
                            int blue = pixel & 0xFF;            // Componente azul

                            sumRed += red;
                            sumGreen += green;
                            sumBlue += blue;
                        }
                    }
                    int pixelCount = width * height;
                    int avgRed = sumRed / pixelCount;
                    int avgGreen = sumGreen / pixelCount;
                    int avgBlue = sumBlue / pixelCount;

                    g.setColor(Color.RED);
                    g.fillRect(graphX, graphY - avgRed, barWidth, avgRed);

                    g.setColor(Color.GREEN);
                    g.fillRect(graphX + barWidth + barSpacing, graphY - avgGreen, barWidth, avgGreen);

                    g.setColor(Color.BLUE);
                    g.fillRect(graphX + 2 * (barWidth + barSpacing), graphY - avgBlue, barWidth, avgBlue);
                }
            };

            // Agregar el panel al marco y mostrarlo
            graphFrame.add(graphPanel);
            graphFrame.setVisible(true);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void increaseRGB() {
        // Obtener la imagen del JLabel
        ImageIcon icon = (ImageIcon) imageLabel.getIcon();
        Image image = icon.getImage();

        // Crear una nueva imagen para modificar los píxeles
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);

        // Obtener los píxeles de la imagen original
        int[] pixels = new int[modifiedImage.getWidth() * modifiedImage.getHeight()];
        PixelGrabber pixelGrabber = new PixelGrabber(image, 0, 0, modifiedImage.getWidth(), modifiedImage.getHeight(), pixels, 0, modifiedImage.getWidth());
        try {
            pixelGrabber.grabPixels();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Aumentar los valores RGB de cada píxel
        for (int i = 0; i < pixels.length; i++) {
            int pixel = pixels[i];
            int red = (pixel >> 16) & 0xFF;
            int green = (pixel >> 8) & 0xFF;
            int blue = pixel & 0xFF;

            // Aumentar los valores en 10 unidades (saturación)
            red = Math.min(red + 10, 255);
            green = Math.min(green + 10, 255);
            blue = Math.min(blue + 10, 255);

            // Crear el nuevo píxel con los valores modificados
            int newPixel = (red << 16) | (green << 8) | blue;
            pixels[i] = newPixel;
        }

        // Establecer los nuevos píxeles en la imagen modificada
        modifiedImage.setRGB(0, 0, modifiedImage.getWidth(), modifiedImage.getHeight(), pixels, 0, modifiedImage.getWidth());

        // Mostrar la imagen modificada en el JLabel
        imageLabel.setIcon(new ImageIcon(modifiedImage));
    }

    private void decreaseRGB() {
        // Obtener la imagen del JLabel
        ImageIcon icon = (ImageIcon) imageLabel.getIcon();
        Image image = icon.getImage();

        // Crear una nueva imagen para modificar los píxeles
        BufferedImage modifiedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);

        // Obtener los píxeles de la imagen original
        int[] pixels = new int[modifiedImage.getWidth() * modifiedImage.getHeight()];
        PixelGrabber pixelGrabber = new PixelGrabber(image, 0, 0, modifiedImage.getWidth(), modifiedImage.getHeight(), pixels, 0, modifiedImage.getWidth());
        try {
            pixelGrabber.grabPixels();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Disminuir los valores RGB de cada píxel
        for (int i = 0; i < pixels.length; i++) {
            int pixel = pixels[i];
            int red = (pixel >> 16) & 0xFF;
            int green = (pixel >> 8) & 0xFF;
            int blue = pixel & 0xFF;

            // Disminuir los valores en 10 unidades (saturación)
            red = Math.max(red - 10, 0);
            green = Math.max(green - 10, 0);
            blue = Math.max(blue - 10, 0);

            // Crear el nuevo píxel con los valores modificados
            int newPixel = (red << 16) | (green << 8) | blue;
            pixels[i] = newPixel;
        }

        // Establecer los nuevos píxeles en la imagen modificada
        modifiedImage.setRGB(0, 0, modifiedImage.getWidth(), modifiedImage.getHeight(), pixels, 0, modifiedImage.getWidth());

        // Mostrar la imagen modificada en el JLabel
        imageLabel.setIcon(new ImageIcon(modifiedImage));
    }

    private void applyNegative() {
        if (icon != null) {
            // Obtener la imagen del icono
            Image image = icon.getImage();

            // Crear una imagen en blanco y negro del mismo tamaño
            BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);

            // Crear un objeto Graphics para dibujar en la imagen en blanco y negro
            Graphics graphics = bufferedImage.getGraphics();
            graphics.drawImage(image, 0, 0, null);
            graphics.dispose();

            // Aplicar el efecto de negativo a la imagen en blanco y negro
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                for (int x = 0; x < bufferedImage.getWidth(); x++) {
                    int rgb = bufferedImage.getRGB(x, y);
                    Color color = new Color(rgb);

                    // Obtener los componentes RGB del color
                    int red = color.getRed();
                    int green = color.getGreen();
                    int blue = color.getBlue();

                    // Calcular el negativo de cada componente RGB
                    int invertedRed = 255 - red;
                    int invertedGreen = 255 - green;
                    int invertedBlue = 255 - blue;

                    // Crear un nuevo color con los componentes invertidos
                    Color invertedColor = new Color(invertedRed, invertedGreen, invertedBlue);

                    // Establecer el nuevo color en la imagen
                    bufferedImage.setRGB(x, y, invertedColor.getRGB());
                }
            }

            // Crear un nuevo icono con la imagen invertida
            ImageIcon invertedIcon = new ImageIcon(bufferedImage);

            // Mostrar la imagen invertida en el JLabel
            imageLabel.setIcon(invertedIcon);

            // Guardar la referencia al nuevo icono para usarlo en otras operaciones
            this.icon = invertedIcon;
        }
    }
    
    private void saveImage() {
        if (icon != null) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar imagen");
            fileChooser.setSelectedFile(new File("imagen_editada.png"));

            int returnValue = fileChooser.showSaveDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File outputFile = fileChooser.getSelectedFile();

                try {
                    BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
                    Graphics graphics = bufferedImage.createGraphics();
                    icon.paintIcon(null, graphics, 0, 0);
                    graphics.dispose();

                    //String extension = getExtension(outputFile.getName());
                    //ImageIO.write(bufferedImage, extension, outputFile);

                    JOptionPane.showMessageDialog(null, "Imagen guardada exitosamente", "Guardar imagen", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error al guardar la imagen: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "No hay una imagen para guardar", "Guardar imagen", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PIM7();
            }
        });
    }
}