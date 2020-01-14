## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-63351"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Windows 10 Security Technical Implementation Guide"


if ($Hostname -eq $null){

$Hostname = $env:computername}

$ServiceName = "McAfeeFramework"
$ServiceStatus = (Get-Service -Name $ServiceName -ErrorAction SilentlyContinue).Status

If ($ServiceStatus -Eq 'Running')
    {
        $Configuration = "Completed"
    } Else {
        $Configuration = "Ongoing"
}

$Audit = New-Object -TypeName System.Object
$Audit | Add-Member -MemberType NoteProperty -Name GroupID -Value $GroupID
$Audit | Add-Member -MemberType NoteProperty -Name Hostname -Value $Hostname
$Audit | Add-Member -MemberType NoteProperty -Name Configuration -Value $Configuration
$Audit | Add-Member -MemberType NoteProperty -Name Title -Value $Title
$Audit